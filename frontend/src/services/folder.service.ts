import type { AxiosResponse } from "axios";
import type { FolderParams } from "../types/folders/folder.params";
import type { Pageable, PageResponse } from "../types/Page.types";
import axios from "axios";
import type { FolderResponse } from "../types/folders/folder.response";
import { FOLDER_REST_API_URL } from "../utils/endpoints";
import type { FolderRequest } from "../types/folders/folder.request";


export const getFolders = async (
    pageable: Pageable,
    params: FolderParams
): Promise<AxiosResponse<PageResponse<FolderResponse>>> => {
    return axios.get<PageResponse<FolderResponse>>(FOLDER_REST_API_URL, {
        params: {
            page: pageable.page,
            size: pageable.size,
            sortBy: pageable.sortBy,
            sortDir: pageable.sortDir,
            query: params.query,
            parentsId: params.parentsIds
        },
        paramsSerializer: (params) => {
            const searchParams = new URLSearchParams();
            for (const key in params) {
                const value = params[key as keyof typeof params];
                if (Array.isArray(value)) {
                    value.forEach((v) => searchParams.append(key, v));
                } else if (value !== undefined && value !== null) {
                    searchParams.append(key, String(value));
                }
            }
            return searchParams.toString();
        }
    });
};

export const getFolder = async (id: string): Promise<AxiosResponse<FolderResponse>> => {
    return axios.get<FolderResponse>(`${FOLDER_REST_API_URL}/${id}`);
};

export const createFolder = async (request: FolderRequest): Promise<AxiosResponse<void>> => {
    return axios.post<void>(FOLDER_REST_API_URL, request);
};

export const deleteFolder = async (id: string): Promise<AxiosResponse<void>> => {
    return axios.delete<void>(`${FOLDER_REST_API_URL}/${id}`);
};