import DashboardCustomizeIcon from '@mui/icons-material/DashboardCustomize';
import FolderIcon from '@mui/icons-material/Folder';
import StyleIcon from '@mui/icons-material/Style';
import InsertDriveFileIcon from '@mui/icons-material/InsertDriveFile';

export const mainListItems = [
    { text: 'Dashboard', icon: <DashboardCustomizeIcon />, href:"/"},
    { text: 'Folders', icon: <FolderIcon />, href:"/folders" },
    { text: 'Files', icon: <InsertDriveFileIcon />, href:"/files" },
    { text: 'tags', icon: <StyleIcon />, href:"/tags" },
]