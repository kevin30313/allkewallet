import axios from "axios";

export const authApi = axios.create({
  baseURL: "https://auth-service-1041045148793.us-central1.run.app/api",
  headers: {
    "Content-Type": "application/json",
  }
});

export const accountApi = axios.create({
  baseURL: "https://account-service-1041045148793.us-central1.run.app/api",
  headers: {
    "Content-Type": "application/json",
  }
});

accountApi.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);
export const getUserByUsername = async (username) => {
  const response = await authApi.get(`/auth/users/${username}`);
  return response.data;
};

export const transferMoney = async (destinationUserId, amount) => {
  const response = await accountApi.post('/accounts/transfer', {
    destinationUserId,
    amount,
  });
  return response.data;
};

export const getMyAccount = async () => {
  const response = await accountApi.get('/accounts/me');
  return response.data;
};

export const getMyTransactions = async () => {
  const response = await accountApi.get('/accounts/me/transactions');
  return response.data;
};
