import { useCreateNewRegistrationWindow, useDeleteRegistrationWindow, useGetAllRegistrationWindows } from "@/api/orval/subject-registration";
import { RegistrationWindowTO } from "@/api/orval/subject-registration.schemas";
import { Snackbar, Alert, Box, Button, Card, DialogActions, DialogContent, DialogTitle, DialogContentText, Stack, TextField, Typography, Dialog } from "@mui/material";
import { DateTimePicker, LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterMoment } from "@mui/x-date-pickers/AdapterMoment";
import { Moment } from "moment";
import { useState } from "react";
import { useQueryClient } from 'react-query';
import DeleteIcon from '@mui/icons-material/Delete';
import { BootstrapDialog, BootstrapDialogTitle } from "./Subjects";

export function WindowPage() {
    const allWindows = useGetAllRegistrationWindows();

    const [dialogOpen, setDialogOpen] = useState<boolean>(false);
    const [createToastOpen, setCreateToastOpen] = useState<boolean>(false);
    const [deleteToastOpen, setDeleteToastOpen] = useState<boolean>(false);

    const createNewWindow = () => {
        setDialogOpen(true);
    }

    return (
        <Box>
            <Box>
                <Typography variant="h4">
                    Anmeldefenster
                </Typography>
                {(allWindows.isSuccess && allWindows.data.length === 0) &&
                    <>
                        <br />
                        <p>
                            Aktuell ist kein Anmeldefenster geöffnet!
                            <br />
                            Falls Sie ein neues Anmeldefenster eröffnen möchten, füllen Sie die nachfolgenden Angaben aus und bestätigen Sie den Anmeldeprozess.
                        </p>
                    </>
                }
                {(allWindows.isSuccess && allWindows.data.length > 0) &&
                    <>
                        <br />
                        <p>
                            Aktuell existieren folgende Anmeldefenster:
                        </p>
                        <Box sx={{ display: 'flex', flexDirection: 'row', justifyContent: 'center', gap: '1.5rem', mt: '2rem', mb: '2rem' }}>
                            {allWindows.data.map((window) => {
                                return <ExistingWindowCard key={window.id} window={window} />
                            })}
                        </Box>
                    </>
                }
                <Button variant="contained" fullWidth onClick={createNewWindow}>Neues Anmeldefenster erstellen</Button>
            </Box>
            {dialogOpen && <NewWindowDialog open={dialogOpen} closeCallback={() => {
                setDialogOpen(false);
                setCreateToastOpen(true);
            }} />}
            <Snackbar open={createToastOpen} autoHideDuration={6000} onClose={() => setCreateToastOpen(false)} anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}>
                <Alert onClose={() => setCreateToastOpen(false)} severity="success" sx={{ width: '100%' }}>
                    Das Anmeldefenster wurde erfolgreich erstellt!
                </Alert>
            </Snackbar>
        </Box>
    );
}

// this is needed since the DateTimePicker always requires an onChange method
function noAction(_: Moment | null) {
}

interface ExistingWindowCardProps {
    window: RegistrationWindowTO;
}

function ExistingWindowCard(props: ExistingWindowCardProps) {
    const [dialogOpen, setDialogOpen] = useState<boolean>(false);

    const deleteWindow = () => {
        setDialogOpen(true);
    };

    return (
        <Box sx={{ display: 'flex', justifyContent: 'center' }}>
            <LocalizationProvider dateAdapter={AdapterMoment}>
                <Card sx={{ maxWidth: '50rem', p: '2rem' }}>
                    <Stack spacing={3}>
                        <TextField value={props.window.semester} label="Semester" size="small" variant="outlined" sx={{}} />
                        <DateTimePicker label="Startzeitpunkt" value={props.window.startDate} onChange={noAction} renderInput={(params) => <TextField size="small" {...params} />} />
                        <DateTimePicker label="Endzeitpunkt" value={props.window.endDate} onChange={noAction} renderInput={(params) => <TextField size="small" {...params} />} />
                        <Button startIcon={<DeleteIcon />} color="error" onClick={deleteWindow} variant="outlined">Anmeldefenster löschen</Button>
                    </Stack>
                </Card>
            </LocalizationProvider>
            {dialogOpen && <DeleteDialog open={dialogOpen} closeCallback={() => setDialogOpen(false)} window={props.window} />}
        </Box>
    );
}

interface NewWindowDialogProps {
    open: boolean;
    closeCallback: () => void;
}

function NewWindowDialog(props: NewWindowDialogProps) {
    const queryClient = useQueryClient();

    const createWindowMutation = useCreateNewRegistrationWindow();

    const [semesterName, setSemesterName] = useState<string>("");
    const [startDateTime, setStartDateTime] = useState<Moment | null>(null);
    const [endDateTime, setEndDateTime] = useState<Moment | null>(null);

    const createNewWindow = () => {
        // @ts-ignore
        createWindowMutation.mutate({ data: { semester: semesterName, startDate: startDateTime.format("YYYY-MM-DThh:mm:ss"), endDate: endDateTime.format("YYYY-MM-DThh:mm:ss"), status: 'active' } })
    };

    if (createWindowMutation.isSuccess) {
        queryClient.invalidateQueries({ queryKey: ['/api/registration_window'], exact: true });
        props.closeCallback();
    }

    return (
        <BootstrapDialog open={props.open}>
            <BootstrapDialogTitle id="customized-dialog-title" onClose={props.closeCallback}>
                Neues Anmeldefenster
            </BootstrapDialogTitle>
            <DialogContent dividers sx={{ maxWidth: '50rem' }}>
                <Box sx={{ display: 'flex', justifyContent: 'center', padding: '1rem' }}>
                    <LocalizationProvider dateAdapter={AdapterMoment}>
                        <Stack spacing={3}>
                            <TextField label="Semester" size="small" variant="outlined" value={semesterName} onChange={event => setSemesterName(event.target.value)} />
                            <DateTimePicker label="Startzeitpunkt" value={startDateTime} onChange={newValue => setStartDateTime(newValue)} renderInput={(params) => <TextField size="small" {...params} />} />
                            <DateTimePicker label="Endzeitpunkt" value={endDateTime} onChange={newValue => setEndDateTime(newValue)} renderInput={(params) => <TextField size="small" {...params} />} />
                            <Button onClick={createNewWindow} variant="contained">Anmeldefenster erstellen</Button>
                        </Stack>
                    </LocalizationProvider>
                </Box>
            </DialogContent>
            <DialogActions>
                <Button autoFocus onClick={props.closeCallback}>
                    Abbrechen
                </Button>
            </DialogActions>
        </BootstrapDialog>
    );
}

interface DeleteDialogProps {
    open: boolean;
    closeCallback: () => void;
    window: RegistrationWindowTO;
}

function DeleteDialog(props: DeleteDialogProps) {
    const queryClient = useQueryClient();

    const deleteWindowMutation = useDeleteRegistrationWindow();

    const deleteWindow = () => {
        deleteWindowMutation.mutate({ registrationWindowId: props.window.id });
    };

    if (deleteWindowMutation.isSuccess) {
        queryClient.invalidateQueries({ queryKey: ['/api/registration_window'], exact: true });
    }

    return (
        <Dialog open={props.open} onClose={props.closeCallback}>
            <DialogTitle id="alert-dialog-title">
                {"Anmeldefenster löschen"}
            </DialogTitle>
            <DialogContent>
                <DialogContentText id="alert-dialog-description">
                    Soll das Anmeldefenster wirklich gelöscht werden?
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button onClick={props.closeCallback}>Abbrechen</Button>
                <Button onClick={deleteWindow} autoFocus>
                    Löschen
                </Button>
            </DialogActions>
        </Dialog>
    );
}