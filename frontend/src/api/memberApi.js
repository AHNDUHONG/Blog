import axiosInstance from "./axiosInstance.js";


export const login = async (loginData) => {
    const response = await axiosInstance.post("/api/members/login", loginData);
    return response.data;
};

export const signup = async (signupData) => {
    const response = await axiosInstance.post("/api/members/signup", signupData);
    return response.data;
};

export const getMyInfo = async () => {
    const response = await axiosInstance.get("/api/members/me");
    return response.data;
};