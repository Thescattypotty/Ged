import axios, { type AxiosResponse } from "axios";
import type { FileParams } from "../types/files/file.params";
import type { FileResponse } from "../types/files/file.response";
import type { Pageable, PageResponse } from "../types/Page.types";
import { FILE_REST_API_URL } from "../utils/endpoints";
import type { FileRequest } from "../types/files/file.request";
import type { FileDto } from "../types/files/file.dto";

export const getFiles = async (
    pageable: Pageable,
    params: FileParams
): Promise<AxiosResponse<PageResponse<FileResponse>>> => {
    return axios.get<PageResponse<FileResponse>>(FILE_REST_API_URL, {
        params: {
            page: pageable.page,
            size: pageable.size,
            sortBy: pageable.sortBy,
            sortDir: pageable.sortDir,
            ...params
        }
    });
};

export const getFile = async (id: string): Promise<AxiosResponse<FileResponse>> => {
    return axios.get<FileResponse>(`${FILE_REST_API_URL}/${id}`);
};

export const downloadFile = async (id: string): Promise<AxiosResponse<FileDto>> => {
    return axios.get<FileDto>(`${FILE_REST_API_URL}/${id}/download`);
};

export const createFile = async (request: FileRequest, file: File): Promise<AxiosResponse<void>> => {
    const formData = new FormData();
    formData.append("file", file);
    formData.append("request", new Blob([JSON.stringify(request)], { type: "application/json" }));
    return axios.post<void>(FILE_REST_API_URL, formData, {
        headers: {
            "Content-Type": "multipart/form-data"
        }
    });
};

export const deleteFile = async (id: string): Promise<AxiosResponse<void>> => {
    return axios.delete<void>(`${FILE_REST_API_URL}/${id}`);
};