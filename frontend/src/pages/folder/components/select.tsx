import { Autocomplete, Box, CircularProgress, FormControl, IconButton, InputLabel, Paper, Stack, TextField, Typography, type SelectChangeEvent } from "@mui/material";
import { useCallback, useEffect, useState, type HTMLAttributes, type ReactNode } from "react";
import useNotifier from "../../../hooks/useNotifier";
import type { Pageable, PageResponse } from "../../../types/Page.types";
import type { FolderParams } from "../../../types/folders/folder.params";
import type { FolderResponse } from "../../../types/folders/folder.response";
import { getFolder, getFolders } from "../../../services/folder.service";
import NavigateNextIcon from '@mui/icons-material/NavigateNext';
import NavigateBeforeIcon from '@mui/icons-material/NavigateBefore';

type PaginationSelectProjectProps = {
    value?: string;
    onChange: (e: SelectChangeEvent<string>, child: ReactNode) => void;
};

export function PaginationSelectProject({
    value,
    onChange,
}: PaginationSelectProjectProps) {
    const notify = useNotifier();
    const [open, setOpen] = useState<boolean>(false);

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

    const [folder, setFolder] = useState<FolderResponse | null>(null);

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

    const loadFolder = async (value: string) => {
        try {
            const response = await getFolder(value);
            setFolder(response.data);
        } catch (error) {
            notify("Error loading folder", "error");
            console.error("Error loading folder:", error);
        }
    };

    useEffect(() => {
        if (value) {
            loadFolder(value);
        }
    }, [value]);

    useEffect(() => {
        loadFolders();
    }, [params, pageable]);


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
                                    page: folders.page.number - 1
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

    return (
        <FormControl fullWidth required sx={{ mt: 4}}>
            <InputLabel
                id="folder-select-label"
                shrink
                sx={{
                    transform: 'translate(0, -1.5rem) scale(0.75)',
                    pointerEvents: 'none'
                }}
            >
                Folder
            </InputLabel>
            <Stack spacing={2} direction={"row"} sx={{ width: '100%' }} alignItems={"center"}>
                <Autocomplete 
                    id="folder-select"
                    open={open}
                    onOpen={() => setOpen(true)}
                    onClose={() => setOpen(false)}
                    value={folder}
                    onChange={(_event, newValue) => {
                        if(newValue){
                            setFolder(newValue);
                            const syntheticEvent = { target: { value: newValue.id, name }, } as unknown as SelectChangeEvent<string>;
                            onChange(syntheticEvent, newValue.id);
                        }
                    }}
                    inputValue={params.query || ''}
                    onInputChange={(_event, newInputValue) => {
                        setParams({
                            ...params,
                            query: newInputValue
                        });
                        setPageable({
                            ...pageable,
                            page: 0
                        });
                    }}
                    isOptionEqualToValue={(option, val) => option.id === val.id}
                    getOptionLabel={(option) => option.name || params.query || ''}
                    options={folders.content}
                    loading={folders.page.totalElements === 0}
                    noOptionsText="Aucun résultat"
                    loadingText="Chargement..."
                    slotProps={{
                        listbox: {
                            component: FolderListBoxComponent
                        }
                    }}
                    renderInput={(params) =>(
                        <TextField 
                            {...params}
                            placeholder="Folders"
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
            </Stack>
        </FormControl>
    );
};