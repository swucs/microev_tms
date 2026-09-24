import { Card, Typography } from 'antd';

export default function PlaceholderPage({ title, desc }: { title: string; desc: string }) {
  return (
    <Card>
      <Typography.Title level={4}>{title}</Typography.Title>
      <Typography.Paragraph type="secondary">{desc}</Typography.Paragraph>
    </Card>
  );
}
