import { useEffect, useState, type ChangeEvent, type FC, type ReactNode } from "react";
import type { FolderRequest } from "../../types/folders/folder.request";
import type { FolderResponse } from "../../types/folders/folder.response";
import { Button, Dialog, DialogActions, DialogContent, DialogTitle, TextField, type SelectChangeEvent } from "@mui/material";
import { PaginationSelectProject } from "./components/select";


interface FolderFormProps {
    open: boolean;
    onClose: () => void;
    onSubmit: (data: FolderRequest) => void;
    initialData?: FolderResponse;
};

export const FolderForm: FC<FolderFormProps> = ({
    open,
    onClose,
    onSubmit,
    initialData
}) => {

    const [formData, setFormData] = useState<FolderRequest>({
        name: '',
        description: '',
        parentId: undefined
    });

    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSelectChange = (e: SelectChangeEvent<string>, _child: ReactNode) => {
        const { value } = e.target;
        setFormData({
            ...formData,
            parentId: value as string
        });
    };

    const handleClose = () => {
        setFormData({
            name: '',
            description: '',
            parentId: undefined
        });
        onClose();
    };

    const handleSubmit = () => {
        onSubmit(formData);
        handleClose();
    };

    useEffect(() => {
        if(initialData){
            setFormData({
                name: initialData.name,
                description: initialData.description,
                parentId: initialData.parent?.id
            });
        }
    }, [initialData]);

    return(
        <Dialog open={open} onClose={handleClose}>
            <DialogTitle>
                {initialData ? "Edit Folder" : "Create Folder"}
            </DialogTitle>
            <DialogContent>
                <TextField 
                    required
                    label="Folder Name"
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                    fullWidth
                    margin="normal"
                />
                <PaginationSelectProject
                    label="Parent Folder"
                    value={formData.parentId}
                    onChange={handleSelectChange}
                />
                <TextField 
                    label="Description"
                    name="description"
                    value={formData.description}
                    onChange={handleChange}
                    fullWidth
                    margin="normal"
                    multiline
                    rows={4}
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose} color="secondary">
                    Cancel
                </Button>
                <Button onClick={handleSubmit} color="primary">
                    Save
                </Button>
            </DialogActions>
        </Dialog>
    )
}