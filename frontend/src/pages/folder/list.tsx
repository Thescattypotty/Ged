import { Box, Divider, IconButton, Stack, Tooltip, Typography } from "@mui/material";
import useNotifier from "../../hooks/useNotifier";
import { useEffect, useState } from "react";
import type { FolderResponse } from "../../types/folders/folder.response";
import type { Pageable, PageResponse } from "../../types/Page.types";
import type { FolderParams } from "../../types/folders/folder.params";
import { getFolders } from "../../services/folder.service";
import { DataGrid, type GridColDef, type GridPaginationModel, type GridRenderCellParams, type GridSortModel } from "@mui/x-data-grid";
import DeleteIcon from '@mui/icons-material/Delete';
import VisibilityIcon from '@mui/icons-material/Visibility';
import RefreshIcon from '@mui/icons-material/Refresh';

export default function FolderList(){

    const notify = useNotifier();
    const [folders, setFolders] = useState<PageResponse<FolderResponse>>({
        content: [],
        page: {
            size: 50,
            number: 0,
            totalElements: 0,
            totalPages: 0
        }
    });

    const [pageable, setPageable] = useState<Pageable>({
        page: 0,
        size: 50,
        sortBy: 'createdAt',
        sortDir: 'desc'
    });

    const [params, setParams] = useState<FolderParams>({
        query: '',
        parentsIds: []
    });
    const [sortModel, setSortModel] = useState<GridSortModel>([]);


    const loadFolders = async () => {
        console.log('Loading folders with params:', params, 'and pageable:', pageable);
        try {
            const response = await getFolders(pageable, params);
            setFolders(response.data);      
        } catch (error) {
            notify("Error loading folders", "error");
            console.error("Error loading folders:", error);
        }
    };

    const handlePageChange = (page: number, size: number) => {
        console.log('Page changed to:', page, 'with size:', size);
        setPageable(prev => ({
            ...prev,
            page: page,
            size: size
        }));
    };

    useEffect(() => {
        console.log('useEffect triggered with pageable:', pageable, 'and params:', params);
        loadFolders();
    }, [pageable, params]);

    const columns: GridColDef<FolderResponse>[] = [
        {
            field: 'name',
            headerName: 'Name',
            flex: 1
        },
        {
            field: 'parent.name',
            headerName: 'Parent',
            flex: 1,
            renderCell: (params: GridRenderCellParams<FolderResponse>) => {
                return params.value ? params.value.name : 'No Parent';
            }
        },
        {
            field: 'path',
            headerName: 'Path',
            flex: 1
        },
        {
            field: 'actions',
            headerName: 'Actions',
            sortable: false,
            flex: 1,
            renderCell: (_params: GridRenderCellParams<FolderResponse>) => (
                <Stack spacing={2} direction='row' sx={{ alignItems: 'center' , paddingTop: 0.5 }}>
                    <IconButton>
                        <VisibilityIcon />
                    </IconButton>
                    <IconButton>
                        <DeleteIcon />
                    </IconButton>
                </Stack>
            )
        }
    ];

    return(
        <Box sx={{ height: '100%', width: '100%'}}>
            <Typography
                component="h2"
                variant="h6"
                sx={{
                    mb: 2,
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    width: '100%'
                }}
            >
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                    Folders
                    <Tooltip title="Refresh">
                        <IconButton onClick={loadFolders}>
                            <RefreshIcon />
                        </IconButton>
                    </Tooltip>
                </Box>
            </Typography>
            <Divider sx={{ m: 2 }} />
            <Divider sx={{ m: 2 }} />
            <Divider sx={{ m: 2 }} />
            <DataGrid 
                columns={columns}
                rows={folders.content}
                disableColumnFilter
                disableColumnMenu
                disableColumnSelector
                disableDensitySelector
                disableRowSelectionOnClick
                disableMultipleRowSelection
                disableColumnResize
                disableEval
                paginationModel={{
                    pageSize: folders.page.size,
                    page: folders.page.number
                }}
                paginationMode="server"
                rowCount={folders.page.totalElements}
                onPaginationModelChange={(model: GridPaginationModel) => {
                    handlePageChange(model.page, model.pageSize);
                }}
                sx={{
                    width: '100%',
                    '& .MuiDataGrid-columnHeader': {
                        backgroundColor: 'background.paper',
                        color: 'background.secondary',
                        fontWeight: 'bold'
                    },
                    '& .MuiDataGrid-columnHeaderTitle': {
                        fontWeight: 'bold'
                    }
                }}
                sortingMode="server"
                sortModel={sortModel}
                onSortModelChange={(model: GridSortModel) => {
                    setSortModel(model);
                    if(model.length > 0) {
                        const { field, sort } = model[0];
                        setPageable(prev => ({
                            ...prev,
                            sortBy: field,
                            sortDir: sort === 'asc' ? 'asc' : 'desc'
                        }));
                    }
                }}
                initialState={{
                    pagination: {
                        paginationModel: {
                            pageSize: folders.page.size,
                            page: folders.page.number
                        }
                    }
                }}
                pageSizeOptions={[10, 25, 50, 100]}
            />
            
        </Box>
    );
};