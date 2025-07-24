
export interface FileResponse {
    id?: string;
    name: string;
    extension: string;
    type: string;
    size: number;
    folderId: string;
    tagsId?: string[];
    idReference: string;
    createdAt: string;
    updatedAt: string;
}