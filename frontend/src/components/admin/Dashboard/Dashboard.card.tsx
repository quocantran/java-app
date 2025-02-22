"use client";
import { IReport } from "@/types/backend";
import { Card, Col, Row, Statistic } from "antd";
import React from "react";
import CountUp from "react-countup";

interface IProps {
  data: IReport;
}

const DashboardCard = (props: IProps) => {
  const { data } = props;
  const formatter = (value: number | string) => {
    return <CountUp end={Number(value)} separator="," />;
  };
  console.log(data);
  return (
    <div>
      <Row gutter={[24, 24]}>
        <Col span={24} md={8}>
          <Card title="Tổng số người dùng" bordered={false}>
            <Statistic
              title="Số lượng người dùng trên hệ thống"
              value={data.totalUsers}
              formatter={formatter}
            />
          </Card>
        </Col>
        <Col span={24} md={8}>
          <Card title="Tổng số công ty" bordered={false}>
            <Statistic
              title="Số lượng công ty trên hệ thống"
              value={data.totalCompanies}
              formatter={formatter}
            />
          </Card>
        </Col>
        <Col span={24} md={8}>
          <Card title="Tổng số công việc" bordered={false}>
            <Statistic
              title="Số lượng công việc trên hệ thống"
              value={data.totalJobs}
              formatter={formatter}
            />
          </Card>
        </Col>
      </Row>
      <div
        style={{
          borderTop: "1px solid rgba(0, 0, 0, 0.45)",
          marginTop: "60px",
        }}
      ></div>
    </div>
  );
};

export default DashboardCard;
