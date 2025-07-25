
export interface FolderResponse {
    id: string;
    name: string;
    parent?: FolderResponse;
    description?: string;
    path: string;
    createdAt: string;
    updatedAt: string;
}