import { useCallback, useEffect, useState, type HTMLAttributes } from "react";
import useNotifier from "../../../hooks/useNotifier";
import type { FolderParams } from "../../../types/folders/folder.params"
import type { FolderResponse } from "../../../types/folders/folder.response";
import type { Pageable, PageResponse } from "../../../types/Page.types";
import { getFolders } from "../../../services/folder.service";
import { Autocomplete, Box, Card, CardActions, CardContent, CircularProgress, FormControl, Grid, IconButton, Paper, TextField, Typography } from "@mui/material";
import NavigateNextIcon from '@mui/icons-material/NavigateNext';
import NavigateBeforeIcon from '@mui/icons-material/NavigateBefore';

export type SearchBarProps = {
    search: FolderParams;
    setSearch: (search: FolderParams) => void;
};


export default function SearchBar({
    search,
    setSearch
}: SearchBarProps) {
    const notify = useNotifier();

    const [openFolder, setOpenFolder] = useState<boolean>(false);
    const [folders, setFolders] = useState<PageResponse<FolderResponse>>({
        content: [],
        page: {
            size: 0,
            number: 0,
            totalElements: 0,
            totalPages: 0
        }
    });

    const [pageable, setPageable] = useState<Pageable>({
        page: 0,
        size: 20,
        sortBy: 'createdAt',
        sortDir: 'desc'
    });

    const [folderParams, setFolderParams] = useState<FolderParams>({
        query: ''
    });

    const loadFolders = async () => {
        console.log('Loading folders with params:', folderParams, 'and pageable:', pageable);
        try {
            // Assuming getFolders is a function that fetches folders based on the params and pageable
            const response = await getFolders(pageable, folderParams);
            setFolders(response.data);
        } catch (error) {
            notify("Error loading folders", "error");
            console.error("Error loading folders:", error);
        }
    };

    const [selectedFolders, setSelectedFolders] = useState<FolderResponse[]>([]);

    useEffect(() => {
        console.log('useEffect triggered with pageable:', pageable, 'and folderParams:', folderParams);
        loadFolders();
    }, [folderParams, pageable]);

    const FolderListBoxComponent = useCallback(
        (props: HTMLAttributes<HTMLUListElement>) => (
            <Paper elevation={8}>
                <ul {...props} />
                <Box
                    component="div"
                    sx={{
                        display: 'flex',
                        justifyContent: 'space-between',
                        alignItems: 'center',
                        width: '100%',
                        p: 1,
                        borderTop: '1px solid',
                        borderColor: 'divider'
                    }}
                    onClick={(e) => {
                        e.stopPropagation();
                        e.preventDefault();
                    }}
                >
                    <IconButton
                        size="small"
                        onClick={(e) => {
                            e.preventDefault();
                            e.stopPropagation();
                            if (folders.page.number > 0) {
                                setPageable({
                                    ...pageable,
                                    page: folders.page.number - 1
                                });
                            }
                        }}
                        disabled={folders.page.number === 0}
                        onMouseDown={(e) => e.preventDefault()}
                    >
                        <NavigateBeforeIcon />
                    </IconButton>
                    <Typography variant="body2">
                        Page {folders.page.number + 1} / {Math.max(1, folders.page.totalPages)}
                    </Typography>
                    <IconButton
                        size="small"
                        onClick={(e) => {
                            e.preventDefault();
                            e.stopPropagation();
                            if (folders.page.number < folders.page.totalPages - 1) {
                                setPageable({
                                    ...pageable,
                                    page: folders.page.number + 1
                                });
                            }
                        }}
                        disabled={folders.page.number >= folders.page.totalPages - 1}
                        onMouseDown={(e) => e.preventDefault()}
                    >
                        <NavigateNextIcon />
                    </IconButton>
                </Box>
            </Paper>
        ),
        [folders.page.number, folders.page.totalPages]
    );

    const handleFolderChange = (newValues: FolderResponse[]) => {
        setSelectedFolders(newValues);
        setSearch({
            ...search,
            parentsIds: newValues.map(folder => folder.id)
        });
    };

    return (
        <Card variant="outlined" sx={{ width: '100%' }}>
            <CardContent>
                <Grid container spacing={2} columns={12} sx={{ mb: (theme) => theme.spacing(2) }}>
                    <FormControl sx={{ width: '42%' }} variant="outlined">
                        <TextField
                            type="text"
                            label="Search Query"
                            value={search.query}
                            onChange={(e) => {
                                setSearch({
                                    ...search,
                                    query: e.target.value
                                });
                            }}
                        />
                    </FormControl>
                    <FormControl sx={{ width: '56%' }} variant="outlined">
                        <Autocomplete
                            id="folder-multi-select"
                            open={openFolder}
                            onOpen={() => setOpenFolder(true)}
                            onClose={() => setOpenFolder(false)}
                            multiple
                            value={selectedFolders}
                            onChange={(_event, newValues) => handleFolderChange(newValues)}
                            inputValue={folderParams?.query || ''}
                            onInputChange={(_event, newInputValue) => {
                                setFolderParams({
                                    ...folderParams,
                                    query: newInputValue
                                });
                                setPageable({
                                    ...pageable,
                                    page: 0
                                })
                            }}
                            isOptionEqualToValue={(option, value) => option.id === value.id}
                            getOptionLabel={(option) => option.name || ''}
                            options={folders.content}
                            loading={folders.page.totalElements === 0}
                            noOptionsText="No folders found"
                            loadingText="Loading folders..."
                            slotProps={{
                                listbox: {
                                    component: FolderListBoxComponent
                                }
                            }}
                            renderInput={(params) => (
                                <TextField
                                    {...params}
                                    label="Select Folders"
                                    placeholder="Search folders..."
                                    variant="outlined"
                                    InputProps={{
                                        ...params.InputProps,
                                        endAdornment: (
                                            <>
                                                {folders.page.totalElements === 0 ? <CircularProgress color="inherit" size={20} /> : null}
                                                {params.InputProps.endAdornment}
                                            </>
                                        ),
                                    }}
                                />
                            )}
                            sx={{ mt: 0, width: '100%' }}
                        />
                    </FormControl>
                </Grid>
            </CardContent>
            <CardActions sx={{ display: 'flex', justifyContent: 'flex-start' }}>
                <IconButton
                    color="primary"
                    onClick={() => {
                        setSearch({
                            query: '',
                            parentsIds: []
                        });
                        setSelectedFolders([]);
                        setFolderParams({ query: '' });
                        setPageable({
                            page: 0,
                            size: 20,
                            sortBy: 'createdAt',
                            sortDir: 'desc'
                        });
                    }}
                    disabled={!search.query && selectedFolders.length === 0}
                >
                    <Typography variant="button">
                        Reset Search Query
                    </Typography>
                </IconButton>
            </CardActions>
        </Card>
    );
};