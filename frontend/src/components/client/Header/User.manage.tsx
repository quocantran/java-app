import {
  Button,
  Form,
  Input,
  Modal,
  Pagination,
  Select,
  Skeleton,
  Table,
  Tabs,
  message,
  notification,
} from "antd";
import { isMobile } from "react-device-detect";
import { Result, TabsProps } from "antd";
import { useState, useEffect } from "react";
import type { ColumnsType, TablePaginationConfig } from "antd/es/table";
import dayjs from "dayjs";
import {
  createSubscriber,
  fetchResumeByUser,
  fetchSkills,
  fetchSubscriberByUser,
  updateUserPassword,
} from "@/config/api";
import { SmileOutlined } from "@ant-design/icons";
import { FormProps } from "antd/lib";
import { useAppSelector } from "@/lib/redux/hooks";
import { IMeta, IResume, ISkill, ISubscribers } from "@/types/backend";
import styles from "../../../styles/Header.module.scss";
import classNames from "classnames/bind";

const cx = classNames.bind(styles);

interface IProps {
  open: boolean;
  setOpen: (v: boolean) => void;
}

const UserResume = (props: any) => {
  const [listCV, setListCV] = useState<IResume[]>([]);
  const [isFetching, setIsFetching] = useState<boolean>(false);
  const [meta, setMeta] = useState<IMeta>();
  const [current, setCurrent] = useState<number>(meta?.current || 1);

  useEffect(() => {
    const fetchData = async () => {
      setIsFetching(true);
      const res = await fetchResumeByUser({ page: current, size: 5 });
      if (res && res.data) {
        const resumes = res.data;
        setMeta(resumes.meta);

        setListCV(res.data.result);
      }
      setIsFetching(false);
    };
    fetchData();
  }, [current]);

  const columns: ColumnsType<IResume> = [
    {
      title: "STT",
      key: "key",
      width: 50,
      align: "center",
      render: (value, record, index) => index + 1,
    },
    {
      title: "Công Ty",
      dataIndex: ["job", "company", "name"],
      width: 150,
    },
    {
      title: "Việc làm",
      dataIndex: ["job", "name"],
      width: 150,
    },
    {
      title: "Trạng thái",
      dataIndex: ["job", "status"],
      width: 100,
    },
    {
      title: "Thời gian nộp",
      dataIndex: ["job", "createdAt"],
      width: 150,
      sorter: (a, b) =>
        dayjs(b.job.createdAt).unix() - dayjs(a.job.createdAt).unix(),
      render: (createdAt: string) =>
        dayjs(createdAt).format("YYYY-MM-DD HH:mm"),
    },
    {
      title: "Thời gian người tuyển dụng cập nhật CV",
      dataIndex: ["job", "updatedAt"],
      key: "resumeUpdatedAt",

      width: 150,
      sorter: (a, b) =>
        dayjs(b.job.updatedAt).unix() - dayjs(a.job.updatedAt).unix(),
      render: (updatedAt: string) =>
        dayjs(updatedAt).format("YYYY-MM-DD HH:mm"),
    },
    {
      title: "",
      dataIndex: "url",
      key: "url",
      render: (value) => (
        <a href={value} target="_blank" rel="noopener noreferrer">
          Chi tiết
        </a>
      ),
      width: 50,
    },
  ];

  return isFetching ? (
    <Skeleton active />
  ) : !listCV.length ? (
    <Result
      icon={<SmileOutlined />}
      title="Bạn chưa rải CV nào trước đây!"
      extra={
        <Button href="/jobs" type="primary">
          Bấm vào đây để tìm việc!
        </Button>
      }
    />
  ) : (
    <div>
      <Table
        bordered={true}
        columns={columns}
        loading={isFetching}
        dataSource={listCV}
        rowKey="key"
        pagination={{
          current: meta?.current || 1,
          pageSize: meta?.pageSize || 1,
          total: meta?.total || 1,
          onChange: (page: number) => setCurrent(page),
        }}
      />
    </div>
  );
};

interface FieldType {
  password: string;
  newPassword: string;
  repeatedPassword: string;
}

const UpdateUserPassword = (props: any) => {
  const userId = useAppSelector((state) => state.auth.user?.id);
  const [loading, setLoading] = useState(false);
  const onFinish: FormProps<FieldType>["onFinish"] = async (values) => {
    const { password, newPassword, repeatedPassword } = values;

    if (newPassword.length < 6) {
      message.error("Mật khẩu mới phải có ít nhất 6 ký tự!");
      return;
    }

    if (newPassword !== repeatedPassword) {
      message.error("Mật khẩu nhập lại không khớp!");
      return;
    }
    setLoading(true);
    const res = await updateUserPassword(values);
    if (res.status === 200) {
      setLoading(false);
      message.success("Thay đổi mật khẩu thành công!");
    } else {
      notification.error({
        message: "Thay đổi mật khẩu thất bại",
        description: res.error,
      });
      setLoading(false);
    }
  };

  const onFinishFailed: FormProps<FieldType>["onFinishFailed"] = (
    errorInfo
  ) => {
    console.log("Failed:", errorInfo);
  };
  return (
    <>
      <h2 style={{ textAlign: "center", marginBottom: 10 }}>
        Thay đổi mật khẩu
      </h2>
      <Form
        name="basic"
        labelCol={{ span: 8 }}
        wrapperCol={{ span: 16 }}
        style={{ maxWidth: 700, margin: "0 auto" }}
        initialValues={{ remember: true }}
        onFinish={onFinish}
        onFinishFailed={onFinishFailed}
        autoComplete="off"
      >
        <Form.Item<FieldType>
          label="Nhập mật khẩu cũ"
          name="password"
          rules={[
            { required: true, message: "Trường này không được để trống!" },
          ]}
        >
          <Input.Password />
        </Form.Item>

        <Form.Item<FieldType>
          label="Nhập mật khẩu mới"
          name="newPassword"
          rules={[
            { required: true, message: "Trường này không được để trống!" },
          ]}
        >
          <Input.Password />
        </Form.Item>

        <Form.Item<FieldType>
          name="repeatedPassword"
          label="Nhập lại mật khẩu mới"
          rules={[
            { required: true, message: "Trường này không được để trống!" },
          ]}
        >
          <Input.Password />
        </Form.Item>

        <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
          <Button loading={loading} type="primary" htmlType="submit">
            Xác nhận
          </Button>
        </Form.Item>
      </Form>
    </>
  );
};

const Subscriber = (props: any) => {
  const [skills, setSkills] = useState<any>([]);
  const [isFetching, setIsFetching] = useState<boolean>(false);
  const [selectedSkills, setSelectedSkills] = useState<any>([]);
  const user = useAppSelector((state) => state.auth.user);
  const [email, setEmail] = useState<string>(user.email || "");
  const [loading, setLoading] = useState<boolean>(false);

  const handleChange = (value: any) => {
    setSelectedSkills(value);
  };

  const handleClick = async () => {
    const data: ISubscribers = {
      email: user.email,
      skills: selectedSkills,
      name: user.name,
    };

    setLoading(true);
    const res = await createSubscriber(data);
    const resData = await res.json();
    if (!res.ok) {
      notification.error({
        message: "Đăng ký thất bại",
        description: resData.message,
      });
      setLoading(false);
      return;
    }
    setLoading(false);
    message.success("Đăng ký thành công!");
  };

  const handleGetEmail = (e: any) => {
    setEmail(e.target.value);
  };

  useEffect(() => {
    const fetchData = async () => {
      setIsFetching(true);
      const res = await fetchSkills({ pageSize: 100, current: 1 });
      const usr = await fetchSubscriberByUser({ email: user.email });
      if (res && res.data) {
        const skillData = res.data.result as ISkill[];
        const skillList = skillData.map((skill) => ({
          label: skill.name,
          value: skill.id,
        }));
        setSkills(skillList);
      }

      if (usr && usr.data) {
        const userSkills = usr.data.skills.map((skill) => skill.id);
        setSelectedSkills(userSkills);
      }
      setIsFetching(false);
    };
    fetchData();
  }, []);

  return isFetching ? (
    <Skeleton></Skeleton>
  ) : (
    <>
      <h2 style={{ textAlign: "center", marginBottom: 10 }}>
        Đăng ký nhận Email công việc theo kỹ năng
      </h2>

      <div style={{ maxWidth: "600px", margin: "0 auto" }}>
        <div style={{ marginBottom: "15px" }}>
          <Input
            value={email}
            onChange={handleGetEmail}
            placeholder="Nhập vào email của bạn"
          />
        </div>

        <div>
          <Select
            mode="multiple"
            placeholder="Lựa chọn kỹ năng"
            onChange={handleChange}
            options={skills}
            defaultValue={selectedSkills}
            style={{ width: "100%" }}
          />
        </div>
        <Button
          onClick={handleClick}
          loading={loading}
          type="primary"
          style={{ marginTop: "15px" }}
        >
          Đăng ký
        </Button>
      </div>
    </>
  );
};

const ManageUser = (props: IProps) => {
  const { open, setOpen } = props;

  const items: TabsProps["items"] = [
    {
      key: "user-resume",
      label: `Danh sách CV đã nộp`,
      children: <UserResume />,
    },
    {
      key: "user-password",
      label: `Thay đổi mật khẩu`,
      children: <UpdateUserPassword />,
    },
    {
      key: "user-subscriber",
      label: `Đăng ký nhận công việc theo kỹ năng`,
      children: <Subscriber />,
    },
  ];

  return (
    <>
      <Modal
        title="Quản lý tài khoản"
        open={open}
        onCancel={() => setOpen(false)}
        maskClosable={false}
        footer={null}
        destroyOnClose={true}
        width={isMobile ? "100%" : "1400px"}
      >
        <div style={{ minHeight: 400 }}>
          <Tabs defaultActiveKey="user-resume" items={items} />
        </div>
      </Modal>
    </>
  );
};

export default ManageUser;
