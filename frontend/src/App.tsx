import { lazy, Suspense } from "react";
import { BrowserRouter } from "react-router-dom";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs"; 

import { SnackbarProvider } from "notistack";
import type { } from '@mui/material/themeCssVarsAugmentation';
import Loading from "./components/Loading";

const AppRoutes = lazy(() => import("./App.routes"));


function App() {
  return (
      <SnackbarProvider
          maxSnack={3}
          anchorOrigin={{
              vertical: 'bottom',
              horizontal: 'right',
          }}
          autoHideDuration={5000}
          preventDuplicate
      >
          <LocalizationProvider dateAdapter={AdapterDayjs}>
              <BrowserRouter>
                      <Suspense fallback={<Loading />}>
                          <AppRoutes />
                      </Suspense>
              </BrowserRouter>
          </LocalizationProvider>
      </SnackbarProvider>
  )
}

export default App
