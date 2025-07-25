import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import Stack from '@mui/material/Stack';

import { Link, useLocation } from 'react-router-dom';
import { Box } from '@mui/material';
import { mainListItems } from '../routes';

export default function MenuContent() {
    const location = useLocation();

    const currentTab = mainListItems.findIndex(tab => location.pathname === tab.href);
    
    return (
        <Stack sx={{ flexGrow: 1, p: 1, justifyContent: 'space-between' }}>
            <Box>
                <List dense>
                    {mainListItems.map((item, index) => (
                        <ListItem key={index} disablePadding sx={{ display: 'block' }}>
                            <Link to={item.href} style={{ textDecoration: 'none', color: 'inherit' }}>
                                <ListItemButton selected={index === currentTab}>
                                    <ListItemIcon>{item.icon}</ListItemIcon>
                                    <ListItemText primary={item.text} />
                                </ListItemButton>
                            </Link>
                        </ListItem>
                    ))}
                </List>
                
            </Box>
        </Stack>
    );
}
