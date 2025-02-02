"use client";

import { IMeta, IResume } from "@/types/backend";
import { Button, Result } from "antd";
import classnames from "classnames/bind";
import styles from "@/styles/JobInfo.module.scss";
import { ColumnsType } from "antd/es/table";
import { Table } from "antd/lib";
import dayjs from "dayjs";
import { SmileOutlined } from "@ant-design/icons";
import React, { useEffect, useState } from "react";
import { isMobile } from "react-device-detect";

const cx = classnames.bind(styles);

interface IProps {
  resumes: IResume[];
  meta?: IMeta;
  isLoading: boolean;
  setCurrent?: (current: number) => void;
}

const JobResume = (props: IProps) => {
  const { resumes, isLoading, meta, setCurrent } = props;

  const handleChangePage = (pagination: any) => {
    setCurrent && setCurrent(pagination.current);
  };

  const columns: ColumnsType<IResume> = [
    {
      title: "STT",
      key: "index",
      width: 50,
      align: "center",
      render: (text, record, index) => {
        return <>{index + 1}</>;
      },
    },
    {
      title: "Công Ty",
      dataIndex: ["job", "company", "name"],
    },
    {
      title: "Vị trí",
      dataIndex: ["job", "name"],
    },
  ];

  if (!isMobile) {
    columns.push({
      title: "Trạng thái",
      dataIndex: ["job", "status"],
    });
    columns.push(
      {
        title: "Ngày rải CV",
        dataIndex: "createdAt",
        sorter: (a, b) =>
          dayjs(a.job.createdAt).unix() - dayjs(b.job.createdAt).unix(),
        render(value, record, index) {
          return (
            <>{dayjs(record.job.createdAt).format("DD-MM-YYYY HH:mm:ss")}</>
          );
        },
      },
      {
        title: "",
        dataIndex: "",
        render(value, record, index) {
          return (
            <a href={record.url} target="_blank">
              Chi tiết
            </a>
          );
        },
      }
    );
  } else {
    columns.slice(0, 3);
  }

  return !resumes?.length ? (
    <Result
      icon={<SmileOutlined />}
      title="Hiện chưa có ai ứng tuyển công việc này!"
      extra={
        <Button href="/jobs" type="primary">
          Bấm vào đây để tìm việc!
        </Button>
      }
    />
  ) : (
    <div className={cx("resume-table")}>
      <h2>Danh sách ứng viên</h2>

      <p className={cx("resume-users")}>
        Hiện có <span>{resumes?.length} ứng viên</span> nộp CV cho công việc này
      </p>

      <div>
        <Table<IResume>
          bordered={true}
          columns={columns}
          dataSource={resumes}
          loading={isLoading}
          pagination={{
            current: meta?.current,
            pageSize: meta?.pageSize,
            total: meta?.total,
          }}
          onChange={handleChangePage}
        />
      </div>
    </div>
  );
};

export default JobResume;
