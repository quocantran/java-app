"use server";
import React from "react";
import classNames from "classnames/bind";
import styles from "../../../styles/Dashboard.module.scss";
import DashboardCard from "@/components/admin/Dashboard/Dashboard.card";
import { fetchReports } from "@/config/api";
import DashboardChart from "@/components/admin/Dashboard/Dashboard.chart";
import { IReport } from "@/types/backend";
import DashboardButton from "@/components/admin/Dashboard/Dashboard.button";

const cx = classNames.bind(styles);

const Admin = async () => {
  const res = await fetchReports({ type: "week" });
  const data = res.data as IReport;
  return (
    <div className={cx("wrapper")}>
      <div className={cx("container")}>
        <DashboardCard data={data} />
      </div>
      <DashboardChart data={data} />
    </div>
  );
};

export default Admin;
