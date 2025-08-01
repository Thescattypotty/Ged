import type { ReactNode } from 'react';
import { Outlet } from 'react-router-dom';
import type { } from '@mui/x-date-pickers/themeAugmentation';
import type { } from '@mui/x-tree-view/themeAugmentation';
import { alpha } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import Stack from '@mui/material/Stack';
import AppTheme from '../theme/AppTheme';
import SideMenu from '../components/SideMenu';
import AppNavbar from '../components/AppNavbar';
import Header from '../components/Header';

interface DashboardProps {
    disableCustomTheme?: boolean;
    children?: ReactNode;
}

export default function DashboardLayout({ disableCustomTheme, children }: DashboardProps) {
    return (
        <AppTheme disableCustomTheme={disableCustomTheme}>
            <CssBaseline enableColorScheme />
            <Box sx={{ display: 'flex' }}>
                <SideMenu />
                <AppNavbar />
                <Box
                    component="main"
                    sx={(theme) => ({
                        flexGrow: 1,
                        backgroundColor: theme.vars
                            ? `rgba(${theme.vars.palette.background.defaultChannel} / 1)`
                            : alpha(theme.palette.background.default, 1),
                        overflow: 'auto',
                    })}
                >
                    <Stack
                        spacing={2}
                        sx={{
                            alignItems: 'center',
                            mx: 3,
                            pb: 5,
                            mt: { xs: 8, md: 0 },
                        }}
                    >
                        <Header />
                        {children ?? <Outlet />}
                    </Stack>
                </Box>
            </Box>
        </AppTheme>
    );
}
