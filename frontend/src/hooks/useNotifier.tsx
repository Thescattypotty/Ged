import { IconButton } from '@mui/material';
import { useSnackbar, VariantType, OptionsObject, SnackbarKey } from 'notistack';
import CloseIcon from '@mui/icons-material/Close';

const useNotifier = () => {
    const { enqueueSnackbar, closeSnackbar } = useSnackbar();

    const notify = (
        message: string,
        variant: VariantType = 'default',
        options: OptionsObject = {}
    ) => {
        enqueueSnackbar(message, {
            variant,
            autoHideDuration: 3000,
            ...options,
            action: (key: SnackbarKey) => (
                <IconButton
                    size="small"
                    aria-label="close"
                    color="inherit"
                    onClick={() => closeSnackbar(key)}
                >
                    <CloseIcon fontSize="small" />
                </IconButton>
            )
        });
    };

    return notify;
};

export default useNotifier;
