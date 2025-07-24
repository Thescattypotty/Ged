export interface PageResponse<T> {
    content: T[];
    page: Page;
};

export interface Page {
    size: number;
    number: number;
    totalElements: number;
    totalPages: number;
};

export interface Pageable {
    page: number;
    size: number;
    sortBy?: string;
    sortDir?: 'asc' | 'desc';
};