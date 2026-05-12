import axiosInstance from "./axiosInstance.js";


export const getPosts = async ({ page = 0, size = 10, keyword="", category = ""}) => {
    const response = await axiosInstance.get("/api/posts", {
        params: {
            page,
            size,
            keyword: keyword || null,
            category: category || null,
        },
    });

    return response.data;
};

export const getPost = async (postId) => {
    const response = await axiosInstance.get("/api/posts", postData);
    return response.data;
};

export const createPost = async (postData) => {
    const response = await axiosInstance.post("/api/posts", postData);
    return response.data;
};

export const updatePost = async (postId, postData) => {
    const response = await axiosInstance.put(`/api/posts/$(postId}`, postData);
    return response.data;
};

export const deletePost = async (postId) => {
    const response = await axiosInstance.delete(`/api/posts/${postId}`);
    return response.data;
};