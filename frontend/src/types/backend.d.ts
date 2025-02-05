export interface IBackendRes<T> {
  status: number | string;
  message?: string;
  error?: string;
  data?: T;
}

export interface IModelPaginate<T> {
  meta: {
    current: number;
    pageSize: number;
    pages: number;
    total: number;
  };
  result: T[];
}

export interface IMeta {
  current: number;
  pageSize: number;
  pages: number;
  total: number;
}

export interface IChat {
  id?: string;
  fileUrl?: string;
  content: string;
  user?: {
    id: string;
    name: string;
  };
  createdBy?: string;
  isDeleted?: boolean;
  deletedAt?: boolean | null;
  createdAt?: string;
  updatedAt?: string;
}

export interface IReport {
  totalUsers: number;
  totalJobs: number;
  totalCompanies: number;
  totalUsersPast: number;
  totalJobsPast: number;
  totalCompaniesPast: number;
}

export interface IAccount {
  access_token: string;
  user: {
    id?: int;
    email: string;
    name: string;
    role: {
      id?: int;
      name: string;
    };
    permissions: {
      id?: int;
      name: string;
      apiPath: string;
      method: string;
      module: string;
    }[];
  };
}

export interface IGetAccount extends Omit<IAccount, "access_token"> {}

export interface ICompany {
  id?: int;
  name: string;
  address: string;
  logo?: string;
  description?: string;
  usersFollowed?: {
    id: int;
    name: string;
  }[];
  createdBy?: string;
  isDeleted?: boolean;
  deletedAt?: boolean | null;
  createdAt?: string;
  updatedAt?: string;
}

export interface IUser {
  id?: int;
  name: string;
  email: string;
  password?: string;
  age: number;
  gender: string;
  address: string;
  role?: {
    id?: int;
    name?: string;
  };

  company?: {
    id?: int;
    name?: string;
  };
  createdBy?: string;
  isDeleted?: boolean;
  deletedAt?: boolean | null;
  createdAt?: string;
  updatedAt?: string;
}

export interface IJob {
  id?: int;
  name: string;
  skills: {
    id: int;
    name: string;
  }[];
  company?: {
    id?: int;
    name: string;
    logo?: string;
    address?: string;
  };
  users?: {
    id: int;
    name: string;
    age: number;
    avatar: string;
  }[];
  location: string;
  salary: number;
  quantity: number;
  level: string;
  description: string;
  startDate: Date;
  endDate: Date;
  active?: boolean;

  createdBy?: string;
  isDeleted?: boolean;
  deletedAt?: boolean | null;
  createdAt?: string;
  updatedAt?: string;
}

export interface IResume {
  id?: int;
  url: string;
  job: {
    id: string;
    name: string;
    location: string;
    level: string;
    status: string;
    updatedAt: string;
    createdAt: string;
    company: {
      name: string;
      address: string;
      logo: string;
    };
  };
}

export interface IUpdateResumeStatus {
  status: string;
  jobId?: string;
}

export interface ICreateResume {
  url?: string;
  companyId: string;
  jobId: string;
  resumeId?: string;
}

export interface IPermission {
  id?: string;
  name: string;
  apiPath: string;
  method: string;
  module: string;

  createdBy?: string;
  isDeleted?: boolean;
  deletedAt?: boolean | null;
  createdAt?: string;
  updatedAt?: string;
}

export interface IRole {
  id?: string;
  name: string;
  description: string;
  active: boolean;
  permissions: IPermission[] | int[];

  createdBy?: string;
  isDeleted?: boolean;
  deletedAt?: boolean | null;
  createdAt?: string;
  updatedAt?: string;
}

export interface IUpdateUserPassword {
  password: string;
  newPassword: string;
  repeatedPassword: string;
}

export interface IJobSuggest {
  name: string;
  location: string;
}

export interface ISubscribers {
  id?: int;
  email: string;
  name: string;
  skills: {
    id: int;
    name: string;
  }[];
  createdBy?: string;
  isDeleted?: boolean;
  deletedAt?: boolean | null;
  createdAt?: string;
  updatedAt?: string;
}

export interface IFile {
  url: string;
}

export interface IComment {
  id?: int;
  content: string;
  user: {
    id?: int;
    name: string;
  };
  company: {
    id?: int;
    name: string;
  };
  parent: string;
  left: number;
  right: number;
  createdBy?: string;
  isDeleted?: boolean;
  deletedAt?: boolean | null;
  createdAt?: string;
  updatedAt?: string;
}

export interface ICreateComment {
  content: string;
  parentId?: int;
  companyId: string;
}

export interface ISkill {
  id?: int;
  name: string;
  createdBy?: string;
  isDeleted?: boolean;
  deletedAt?: boolean | null;
  createdAt?: string;
  updatedAt?: string;
}

export interface INotification{
  id: string;
  senderId : string;
  receiverId : string;
  content : string;
  type : string;
  options: {
    jobId?: string;
  }
  createdAt: string;
  updatedAt: string;
}