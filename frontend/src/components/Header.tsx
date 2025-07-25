import Stack from '@mui/material/Stack';
import ColorModeIconDropdown from '../theme/ColorModeIconDropdown';


export default function Header() {
    return (
        <Stack
            direction="row"
            sx={{
                display: { xs: 'none', md: 'flex' },
                width: '100%',
                alignItems: { xs: 'flex-end', md: 'center' },
                justifyContent: 'flex-end',
                maxWidth: { sm: '100%', md: '1700px' },
                pt: 1.5,
            }}
            spacing={2}
        >
            <Stack direction="row" sx={{ gap: 1 }}>
                <ColorModeIconDropdown />
            </Stack>
        </Stack>
    );
}
