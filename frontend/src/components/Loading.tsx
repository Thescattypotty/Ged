import { Box, CircularProgress } from "@mui/material";

export default function Loading() {
    return (
        <Box
            display="flex"
            alignItems="center"
            justifyContent="center"
            height="100vh"
            width="100vw"
            sx={{ backgroundColor: 'background.default' }}
        >
            <CircularProgress size={60} thickness={5} color="primary" />
        </Box>
    );
}
