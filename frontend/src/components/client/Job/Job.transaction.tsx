"use client";

import React, { useEffect, useState } from "react";
import classNames from "classnames/bind";
import styles from "../../../styles/JobInfo.module.scss";
import { Flex, message, Modal, notification, Spin, Tag } from "antd";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import dayjs from "dayjs";
import { faEye, faL } from "@fortawesome/free-solid-svg-icons";
import { IJob, IMeta, IResume } from "@/types/backend";
import socket from "@/utils/socket";
import { generateRandomCode } from "@/helpers";
import { useAppSelector } from "@/lib/redux/hooks";
import { LoadingOutlined } from "@ant-design/icons";
import { fetchResumeByJob } from "@/config/api";
import JobResume from "./Job.resume";

const cx = classNames.bind(styles);

interface IProps {
  job: IJob;
}

const JobTransaction = (props: IProps) => {
  const { job } = props;
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const [paymentCode, setPaymentCode] = useState<string>("");
  const [qrCode, setQrCode] = useState<string>("");
  const isAuth = useAppSelector((state) => state.auth.isAuthenticated);
  const user = useAppSelector((state) => state.auth.user);
  const [paymentStatus, setPaymentStatus] = useState<boolean>(false);
  const [hasPaid, setHasPaid] = useState<boolean>(false);
  const [loading, setLoading] = useState<boolean>(false);
  const [isFetchingResume, setIsFetchingResume] = useState<boolean>(false);
  const [usersCv, setUsersCv] = useState<IResume[]>();
  const [meta, setMeta] = useState<IMeta>();
  const [current, setCurrent] = useState<number>(1);

  useEffect(() => {
    if (paymentStatus) return;
    if (hasPaid) return;

    if (isAuth) {
      let interval: NodeJS.Timeout;
      let timeout: NodeJS.Timeout;
      if (!paymentCode) {
        setPaymentCode(generateRandomCode());
      }

      if (isModalOpen && paymentCode && !paymentStatus && !hasPaid) {
        // Đợi 10 giây trước khi bắt đầu interval
        timeout = setTimeout(() => {
          interval = setInterval(() => {
            if (hasPaid) {
              clearInterval(interval);
              clearTimeout(timeout);
            }
            socket.emit("checkPayment", {
              code: paymentCode,
              userId: user.id,
              jobId: job.id,
            });
          }, 1000*30);
        },1000*15);
      }

      return () => {
        clearInterval(interval);
        clearTimeout(timeout);
      };
    }
  }, [isModalOpen, paymentCode, hasPaid]);

  useEffect(() => {
    setHasPaid(job.users?.some((e) => e.id === user.id) as boolean);
  }, [user]);

  useEffect(() => {
    if (hasPaid && isModalOpen) {
      setIsFetchingResume(true);
      const fetchData = async () => {
        const res = await fetchResumeByJob({
          jobId: ("" + job.id) as string,
          size: "5",
          page: "" + current,
        });

        setUsersCv(res.data?.result as unknown as IResume[]);
        setMeta(res.data?.meta as IMeta);

        setIsFetchingResume(false);
      };

      fetchData();
    }
  }, [hasPaid, isModalOpen, current]);

  useEffect(() => {
    if (paymentCode) {
      setQrCode(
        `https://img.vietqr.io/image/MB-0398273537-compact2.png?amount=2000&addInfo=${paymentCode}&accountName=NGUYEN MINH PHUC`
      );
    }
  }, [paymentCode]);

  useEffect(() => {
    if (paymentStatus) {
      notification.success({
        message: "Thanh toán thành công!",
        description: "Vui lòng chờ trong giây lát!",
      });
    }
  }, [paymentStatus]);

  useEffect(() => {
    if (isModalOpen) {
      const handleCheckPayment = (data: any) => {
        setPaymentStatus(!!data?.status);
        setHasPaid(!!data?.status);
        console.log(data);
      };
      socket.on("checkPayment", handleCheckPayment);

      return () => {
        socket.off("checkPayment", handleCheckPayment);
      };
    }
  }, [isModalOpen]);

  const showModal = () => {
    if (!isAuth) {
      message.error("Vui lòng đăng nhập để sử dụng tính năng này!");

      return;
    }
    setIsModalOpen(true);
  };

  const handleOk = () => {
    setIsModalOpen(false);
  };

  const handleCancel = () => {
    setIsModalOpen(false);
  };
  return (
    <div
      style={{ display: "inline-block", cursor: "pointer" }}
      className={cx("view-wrapper")}
    >
      <Tag
        onClick={showModal}
        style={{ border: "1px solid #99e0b9" }}
        color="#fff"
      >
        <span style={{ color: "#00b14f" }} className={cx("tag-title")}>
          <FontAwesomeIcon icon={faEye} />
          Xem số lượng người đã ứng tuyển
        </span>
      </Tag>

      <Modal
        open={isModalOpen}
        width={900}
        onOk={handleOk}
        onCancel={handleCancel}
        okText="Xác nhận"
      >
        <div className={cx("modal-payment")}>
          <>
            {hasPaid ? (
              isFetchingResume ? (
                <Flex align="center" justify="center" gap="middle">
                  <Spin
                    indicator={
                      <LoadingOutlined style={{ fontSize: 48 }} spin />
                    }
                  />
                </Flex>
              ) : (
                <JobResume
                  resumes={usersCv as IResume[]}
                  meta={meta}
                  setCurrent={setCurrent}
                  isLoading={isFetchingResume}
                />
              )
            ) : loading ? (
              <div
                style={{
                  height: 500,
                  margin: "auto",
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                }}
              >
                <div>
                  <Flex align="center" justify="center" gap="middle">
                    <Spin
                      indicator={
                        <LoadingOutlined style={{ fontSize: 48 }} spin />
                      }
                    />
                  </Flex>
                  <p
                    style={{
                      textAlign: "center",
                      marginTop: "15px",
                      fontSize: "22px",
                      color: "#00b14f",
                    }}
                  >
                    Thanh toán thành công! Vui lòng đợi trong giây lát...
                  </p>
                </div>
              </div>
            ) : (
              <>
                <h2>
                  Thanh toán dịch vụ: <span> Xem số người đã ứng tuyển</span>
                </h2>
                <div className={cx("payment-inner")}>
                  <h3>
                    Số tiền thanh toán: <span>2.000VNĐ</span>
                  </h3>
                  <div className={cx("payment-title")}>
                    Sử dụng <span>Ứng dụng ngân hàng </span>
                    và quét mã QR để thanh toán
                  </div>

                  <div className={cx("payment-title")}>
                    Mã đơn hàng: <span>{paymentCode}</span>
                  </div>

                  <div className={cx("payment-qr")}>
                    <div className={cx("qr-code")}>
                      <img src={qrCode} alt="qr-code" />
                    </div>

                    <div className={cx("qr-content")}>
                      <div className={cx("qr-desc")}>
                        <span>Quét mã QR để thanh toán</span>
                      </div>
                      <div className={cx("qr-title")}>
                        <span>
                          Sau khi thanh toán xong vui lòng không tắt trình duyệt và bảng thanh toán và chờ vài giây để hệ
                          thống xác nhận!
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              </>
            )}
          </>
        </div>
      </Modal>
    </div>
  );
};

export default JobTransaction;
