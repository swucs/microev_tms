package com.obigo.microev.tms.admin.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obigo.microev.tms.admin.presentation.common.GetCoordinateReqDto;
import com.obigo.microev.tms.admin.presentation.common.GetCoordinateResDto;
import com.obigo.microev.tms.admin.presentation.common.GetEupMyeonDongReqDto;
import com.obigo.microev.tms.admin.presentation.common.GetEupMyeonDongResDto;
import com.obigo.microev.tms.admin.service.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CommonServiceImpl implements CommonService {

    private final WebClient webClient;

    @Value("${external.kakao-api.domain}")
    private String kakaoApiDomain;

    @Value("${external.kakao-api.key}")
    private String kakaoApiKey;

    @Value("${external.kakao-api.address.url}")
    private String kakaoApiAddressUrl;


    /**
     * 주소를 통해 좌표를 가져온다.
     * @param reqDto
     * @return
     */
    @Override
    public GetCoordinateResDto getCoordinate(GetCoordinateReqDto reqDto) throws Exception {
        log.info("Get Coordinate : {}", reqDto);
        String encoded = URLEncoder.encode(reqDto.getAddress(), StandardCharsets.UTF_8);

        String response =  webClient.method(HttpMethod.GET)
                .uri(kakaoApiDomain + kakaoApiAddressUrl  + encoded)
                .headers(httpHeaders -> {
                    httpHeaders.set("Authorization", "KakaoAK " + kakaoApiKey);
                })
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("Response : {}", response);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(response);
        JsonNode documents = root.path("documents");
        if (documents == null || documents.isEmpty()) {
            return null;
        }

        JsonNode document = documents.get(0);

        GetCoordinateResDto resDto = new GetCoordinateResDto();
        resDto.setLongitude(document.path("x").asText());
        resDto.setLatitude(document.path("y").asText());

        return resDto;

    }


    @Override
    public GetEupMyeonDongResDto getEupMyeonDong(GetEupMyeonDongReqDto reqDto) throws Exception {
        log.info("getEupMyeonDong : {}", reqDto);
        String encoded = URLEncoder.encode(reqDto.getAddress(), StandardCharsets.UTF_8);

        String response =  webClient.method(HttpMethod.GET)
                .uri(kakaoApiDomain + kakaoApiAddressUrl  + encoded)
                .headers(httpHeaders -> {
                    httpHeaders.set("Authorization", "KakaoAK " + kakaoApiKey);
                })
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("Response : {}", response);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(response);
        JsonNode documents = root.path("documents");
        if (documents == null || documents.isEmpty()) {
            return null;
        }

        JsonNode document = documents.get(0);

        JsonNode address = document.path("address");
        GetEupMyeonDongResDto resDto = convertToEupMyeonDong(address);
        if (resDto != null) {
            return resDto;
        }

        JsonNode roadAddress = document.path("road_address");
        resDto = convertToEupMyeonDong(roadAddress);
        if (resDto != null) {
            return resDto;
        }

        return null;
    }


    private GetEupMyeonDongResDto convertToEupMyeonDong(JsonNode address) {
        String region1depthName = address.path("region_1depth_name").asText();
        String region2depthName = address.path("region_2depth_name").asText();
        String region3depthName = address.path("region_3depth_name").asText();
        String region3depthHName = address.path("region_3depth_h_name").asText();    //행정동


        if (!StringUtils.isBlank(region1depthName)) {
            GetEupMyeonDongResDto resDto = new GetEupMyeonDongResDto();
            resDto.setEupMyeonDong(region1depthName + " " + region2depthName + " " + StringUtils.defaultIfBlank(region3depthName, region3depthHName));
            return resDto;
        }

        return null;
    }
}
