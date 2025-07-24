import type { AxiosResponse } from "axios";
import type { TagParams } from "../types/tags/tag.params";
import type { Pageable, PageResponse } from "../types/Page.types";
import type { TagResponse } from "../types/tags/tag.response";
import axios from "axios";
import { TAG_REST_API_URL } from "../utils/endpoints";
import type { TagRequest } from "../types/tags/tag.request";



export const getTags = async (
    pageable: Pageable,
    params: TagParams
): Promise<AxiosResponse<PageResponse<TagResponse>>> => {
    return axios.get<PageResponse<TagResponse>>(TAG_REST_API_URL, {
        params: {
            page: pageable.page,
            size: pageable.size,
            sortBy: pageable.sortBy,
            sortDir: pageable.sortDir,
            params
        }
    });
};

export const getTag = async (id: string): Promise<AxiosResponse<TagResponse>> => {
    return axios.get<TagResponse>(`${TAG_REST_API_URL}/${id}`);
};

export const createTag = async (request: TagRequest): Promise<AxiosResponse<void>> => {
    return axios.post<void>(TAG_REST_API_URL, request);
};

export const deleteTag = async (id: string): Promise<AxiosResponse<void>> => {
    return axios.delete<void>(`${TAG_REST_API_URL}/${id}`);
};