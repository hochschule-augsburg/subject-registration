import * as React from 'react';
import Box from '@mui/material/Box';
import Stepper from '@mui/material/Stepper';
import Step from '@mui/material/Step';
import StepButton from '@mui/material/StepButton';
import Button from '@mui/material/Button';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import Typography from '@mui/material/Typography';
import { SetStateAction, useEffect, useState } from 'react';
import AddIcon from '@mui/icons-material/Add';
import { SubjectTO, NewRegistrationTO } from '@/api/orval/subject-registration.schemas';
import { useCreateNewRegistration, useDeleteRegistration, useGetAllSubjects, useGetRegistration, useUpdateRegistration } from '@/api/orval/subject-registration';
import { BootstrapDialog, BootstrapDialogTitle } from './Subjects';
import { Alert, Card, CardActions, CardContent, DialogActions, DialogContent, Divider, ListSubheader, Snackbar, TextField } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import { useMap } from 'usehooks-ts';

const steps = ['Auswahl der Wahlpflichtfächer', 'Vergabe der Prioritätenpunkte', 'Überblick'];

interface StepperPageProps {
    userId: string | undefined;
}

export function StepperPage(props: StepperPageProps) {
    const [activeStep, setActiveStep] = React.useState(0);
    const [completed, setCompleted] = React.useState<{
        [k: number]: boolean;
    }>({});

    const [toastOpen, setToastOpen] = useState(false);
    const [registrationId, setRegistrationId] = useState<string | undefined>();

    const [selectedSubjects, setSelectedSubjects] = useState<SubjectTO[]>([]);
    const [subjectPriority, subjectPriorityActions] = useMap<string, number>();
    const [targetCreditPoints, setTargetCreditPoints] = useState<number>(0);

    const [firstStepError, setFirstStepError] = useState(false);
    const [secondStepError, setSecondStepError] = useState(false);

    const previousRegistration = useGetRegistration();

    let totalPriority = 0;
    subjectPriority.forEach(value => {
        totalPriority += value;
    });

    const totalSteps = () => {
        return steps.length;
    };

    const completedSteps = () => {
        return Object.keys(completed).length;
    };

    const isLastStep = () => {
        return activeStep === totalSteps() - 1;
    };

    const allStepsCompleted = () => {
        return completedSteps() === totalSteps();
    };

    const handleNext = () => {
        const newActiveStep =
            isLastStep() && !allStepsCompleted()
                ? steps.findIndex((step, i) => !(i in completed))
                : activeStep + 1;
        setActiveStep(newActiveStep);
    };

    const handleBack = () => {
        setActiveStep((prevActiveStep) => prevActiveStep - 1);
    };

    const handleStep = (step: number) => () => {
        setActiveStep(step);
    };

    const handleComplete = () => {
        if (activeStep === 2) {
            if (props.userId) {
                const subjects = [];

                for (let d of selectedSubjects) {
                    let prio = subjectPriority.get(d.id) || 0;
                    subjects.push({ subject: d.id, points: prio });
                }

                if (previousRegistration.isSuccess && previousRegistration.data.id) {
                    const data = { id: props.userId || "", subjectSelection: subjects };

                    putMutation.mutate({ data: data });
                } else {
                    const data = { subjectSelection: subjects };
                    postMutation.mutate({ data: data });
                }
                setToastOpen(true);
            }
        } else {
            const newCompleted = completed;
            newCompleted[activeStep] = true;
            setCompleted(newCompleted);
            handleNext();
        }
    };

    const handleReset = () => {
        setActiveStep(0);
        setCompleted({});
    };

    const nextDisabled = (activeStep === 0 && firstStepError) || (activeStep === 1 && secondStepError);

    const postMutation = useCreateNewRegistration();
    const putMutation = useUpdateRegistration();

    if (postMutation.isSuccess) {
        setRegistrationId(postMutation.data.id);
    }

    if (previousRegistration.isSuccess) {
        // stub
    }

    if (previousRegistration.isError) {
        // stub
    }

    return (
        <Box>
            <Card sx={{ padding: '5rem' }}>
                <Box sx={{ width: '100%', maxWidth: '90rem' }}>
                    <Stepper activeStep={activeStep}>
                        {steps.map((label, index) => (
                            <Step key={label} completed={completed[index]} >
                                <StepButton color="inherit" onClick={handleStep(index)}>
                                    {label}
                                </StepButton>
                            </Step>
                        ))}
                    </Stepper>
                    <div>
                        {allStepsCompleted() ? (
                            <React.Fragment>
                                <Typography sx={{ mt: 2, mb: 1 }}>
                                    All steps completed - you&apos;re finished
                                </Typography>
                                <Box sx={{ display: 'flex', flexDirection: 'row', pt: 2 }}>
                                    <Box sx={{ flex: '1 1 auto' }} />
                                    <Button onClick={handleReset}>Reset</Button>
                                </Box>
                            </React.Fragment>
                        ) : (
                            <React.Fragment>
                                {activeStep === 0 &&
                                    <FirstStep selectedSubjects={selectedSubjects} removeSubjectPriority={subjectPriorityActions.remove} setSelectedSubjects={setSelectedSubjects} errorCallback={setFirstStepError} />
                                }
                                {activeStep === 1 &&
                                    <SecondStep selectedSubjects={selectedSubjects} creditPointsCallback={setTargetCreditPoints} setSubjectPriority={subjectPriorityActions.set} totalPriority={totalPriority} errorCallback={setSecondStepError} defaultCreditPoints={targetCreditPoints} subjectPriority={subjectPriority} />
                                }
                                {activeStep === 2 &&
                                    <ThirdStep selectedSubjects={selectedSubjects} targetCreditPoints={targetCreditPoints} subjectPriority={subjectPriority} />
                                }
                                <Box sx={{ display: 'flex', flexDirection: 'row', pt: 2, justifyContent: 'center' }}>
                                    {activeStep !== 0 && <Button
                                        color="inherit"
                                        disabled={activeStep === 0}
                                        onClick={handleBack}
                                        sx={{ mr: 1 }}
                                    >
                                        Zurück
                                    </Button>}
                                    <Button onClick={handleComplete} sx={{ mr: 1 }} disabled={nextDisabled}>
                                        {activeStep === 2 ? "Bestätigen" : "Weiter"}
                                    </Button>
                                </Box>
                            </React.Fragment>
                        )}
                    </div>
                    {toastOpen && <Alert onClose={() => setToastOpen(false)} severity="success" sx={{ maxWidth: '30rem', ml: 'auto', mr: 'auto' }}>
                        Die Registrierung wurde erfolgreich gespeichert!
                    </Alert>}
                </Box>
                <Snackbar open={toastOpen} autoHideDuration={6000} onClose={() => setToastOpen(false)} anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}>
                    <Alert onClose={() => setToastOpen(false)} severity="success" sx={{ width: '100%' }}>
                        Die Registrierung wurde erfolgreich gespeichert!
                    </Alert>
                </Snackbar>
            </Card>
        </Box>
    );
}

interface FirstStepProps {
    selectedSubjects: SubjectTO[];
    setSelectedSubjects: React.Dispatch<SetStateAction<SubjectTO[]>>;
    errorCallback: (error: boolean) => void;
    removeSubjectPriority: (subject: string) => void;
}

function FirstStep(props: FirstStepProps) {
    const { isLoading, data: subjects } = useGetAllSubjects();

    const [dialogOpen, setDialogOpen] = useState<boolean>(false);

    useEffect(() => {
        props.errorCallback(props.selectedSubjects.length === 0);
    }, [props.selectedSubjects]);

    const addSubject = () => {
        setDialogOpen(true);
    };

    const selectSubject = (subject: SubjectTO) => {
        props.setSelectedSubjects(oldSubjects => [...oldSubjects, subject]);
    };

    const removeSubject = (subject: SubjectTO) => {
        props.setSelectedSubjects(oldSubjects => oldSubjects.filter(s => s.id !== subject.id));
        props.removeSubjectPriority(subject.id);
    }

    return (
        <Box sx={{ display: 'flex', justifyContent: 'center', flexDirection: 'column' }}>
            <Box sx={{ display: 'flex', mt: 4, justifyContent: 'center' }} >
                <Typography sx={{ mt: 4, mb: 1 }}>
                    Im ersten Schritt wählen Sie bitte die Wahlpflichtfächer aus, die Sie in diesem Semester belegen möchten.
                </Typography>
            </Box>
            {props.selectedSubjects.length == 0 ? (
                <Box sx={{ display: 'flex', mt: 4, justifyContent: 'center' }} >
                    <Alert severity="error">
                        Aktuell sind noch keine Wahlpflichtfächer ausgewählt.
                    </Alert>
                </Box>
            ) : (
                <Box sx={{ display: 'flex', justifyContent: 'center', maxWidth: '70rem', flexDirection: 'column', ml: 'auto', mr: 'auto' }}>
                    {props.selectedSubjects.map((subject, index) => {
                        return (
                            <Card variant="outlined" key={index} sx={{ mb: 1 }}>
                                <CardContent>
                                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                                        {"Bachelor (" + subject.creditPoints + " CP)"}
                                    </Typography>
                                    <Typography variant="h5" component="div">
                                        {subject.name}
                                    </Typography>
                                    <Typography sx={{ mb: 1.5 }} color="text.secondary">
                                        {subject.professor}
                                    </Typography>
                                </CardContent>
                                <CardActions>
                                    <Button variant="outlined" color="error" startIcon={<DeleteIcon />} onClick={() => removeSubject(subject)}>Entfernen</Button>
                                </CardActions>
                            </Card>
                        );
                    })}
                </Box>
            )}
            <Box sx={{ display: 'flex', mt: 4, justifyContent: 'center' }} >
                <Button variant="contained" startIcon={<AddIcon />} onClick={addSubject} sx={{ width: '100%', maxWidth: '40rem' }}>
                    Wahlpflichtfach hinzufügen
                </Button>
            </Box>
            {subjects !== undefined && <SubjectSelectionDialog subjects={subjects.filter(s => !props.selectedSubjects.includes(s))} open={dialogOpen} closeCallback={() => setDialogOpen(false)} selectedSubject={selectSubject} />}
        </Box>
    );
}

interface SubjectSelectionDialogProps {
    subjects: SubjectTO[];
    open: boolean;
    closeCallback: () => void;
    selectedSubject: (subject: SubjectTO) => void;
}

function SubjectSelectionDialog(props: SubjectSelectionDialogProps) {
    const list = props.subjects.map((subject, index) => {
        return (<Card variant="outlined" key={index} sx={{ mb: '1rem' }}>
            <CardContent>
                <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                    {"Bachelor (" + subject.creditPoints + " CP)"}
                </Typography>
                <Typography variant="h5" component="div">
                    {subject.name}
                </Typography>
                <Typography sx={{ mb: 1.5 }} color="text.secondary">
                    {subject.professor}
                </Typography>
                <Typography variant="body2" sx={{
                    display: '-webkit-box',
                    overflow: 'hidden',
                    WebkitBoxOrient: 'vertical',
                    WebkitLineClamp: 6,
                }}>
                    {subject.description}
                </Typography>
            </CardContent>
            <CardActions>
                <Button variant="outlined" startIcon={<AddIcon />} onClick={() => props.selectedSubject(subject)} sx={{ ml: '0.5rem' }}>Wahlpflichtfach Hinzufügen</Button>
            </CardActions>
        </Card>);
    });

    return (
        <BootstrapDialog open={props.open}>
            <BootstrapDialogTitle id="customized-dialog-title" onClose={props.closeCallback}>
                Wahlpflichtfachauswahl
            </BootstrapDialogTitle>
            <DialogContent dividers sx={{ maxWidth: '50rem' }}>
                {list}
            </DialogContent>
            <DialogActions>
                <Button autoFocus onClick={props.closeCallback}>
                    Auswahl abbrechen
                </Button>
            </DialogActions>
        </BootstrapDialog>
    );
}

interface SecondStepProps {
    selectedSubjects: SubjectTO[];
    creditPointsCallback: (input: number) => void;
    setSubjectPriority: (subject: string, priority: number) => void;
    totalPriority: number;
    errorCallback: (error: boolean) => void;
    defaultCreditPoints: number;
    subjectPriority: Omit<Map<string, number>, "set" | "clear" | "delete">;
}

function SecondStep(props: SecondStepProps) {
    const [creditPointsError, setCreditPointsError] = useState<boolean>(false);

    useEffect(() => {
        if (props.totalPriority !== 1000 || creditPointsError) {
            props.errorCallback(true);
        } else {
            props.errorCallback(false);
        }
    }, [props.totalPriority]);

    return (
        <Box sx={{ display: 'flex', justifyContent: 'center', flexDirection: 'column' }}>
            <Box sx={{ display: 'flex', mt: 4, justifyContent: 'center' }}>
                <Typography sx={{ mr: 2, mt: 1, ml: 1 }}>
                    Geben Sie zunächst im nachfolgenden Kästchen an, wie viele Creditpoints Sie in diesem Semester belegen möchten:
                </Typography>
                <TextField defaultValue={props.defaultCreditPoints} helperText={creditPointsError ? "Zahl zwischen 0 und 99" : ""} error={creditPointsError} id="creditPointsInput" label="Credit Points" variant="outlined" inputProps={{ type: 'text', min: 0, inputMode: 'numeric', pattern: '[0-9]*' }} size="small" sx={{ maxWidth: '11rem' }} onChange={e => {
                    let parsed = parseInt(e.target.value);
                    if (isNaN(parsed) || parsed >= 100 || parsed < 0) {
                        setCreditPointsError(true);
                    } else {
                        setCreditPointsError(false);
                        props.creditPointsCallback(parsed);
                    }
                }} />
            </Box>
            <Box sx={{ display: 'flex', mt: 4, justifyContent: 'center' }}>
                <Typography sx={{ mr: 2, mt: 1, ml: 1 }}>
                    Vergeben Sie nun Prioritätspunkte für Ihre ausgewählten Fächer, sodass die Summe genau 1000 ergibt!
                </Typography>
            </Box>
            <Box sx={{ display: 'flex', mt: 4, justifyContent: 'center' }}>
                <List subheader={<ListSubheader>Wahlpflichtfächer</ListSubheader>}>
                    {props.selectedSubjects.map((s, index) => {
                        return (
                            <>
                                <PriorityComponent key={index} subject={s} index={index} setSubjectPriority={props.setSubjectPriority} defaultPriority={props.subjectPriority.get(s.id)} />
                            </>
                        );
                    })}
                </List>
            </Box>
            {(props.totalPriority < 1000 || props.totalPriority > 1000) && <Alert severity='error' sx={{ ml: 'auto', mr: 'auto' }}>Die Gesamtsumme der Prioritäten muss 1000 ergeben!</Alert>}
        </Box>
    );
}

interface PriorityComponentProps {
    subject: SubjectTO;
    index: number;
    defaultPriority: number | undefined;
    setSubjectPriority: (subject: string, priority: number) => void;
}

function PriorityComponent(props: PriorityComponentProps) {
    const [priorityError, setPriorityError] = useState(false);

    return (
        <>
            <ListItem key={props.index}>
                <ListItemText primary={props.subject.name} secondary={props.subject.professor} />
                <TextField helperText={priorityError ? "Zahl zwischen 1 und 1000" : ""} defaultValue={props.defaultPriority} error={priorityError} label="Priorität" variant="outlined" type="number" size="small" sx={{ maxWidth: '10rem', ml: 10 }} onChange={e => {
                    let parsed = parseInt(e.target.value);
                    if (isNaN(parsed) || parsed > 1000 || parsed < 1) {
                        setPriorityError(true);
                        props.setSubjectPriority(props.subject.id, 0);
                    } else {
                        setPriorityError(false);
                        props.setSubjectPriority(props.subject.id, parsed);
                    }
                }} />
            </ListItem>
            <Divider variant="inset" component="li"></Divider>
        </>
    );
}

interface ThirdStepProps {
    selectedSubjects: SubjectTO[];
    targetCreditPoints: number;
    subjectPriority: Omit<Map<string, number>, "set" | "clear" | "delete">;
}

function ThirdStep(props: ThirdStepProps) {
    return (
        <Box sx={{ display: 'flex', justifyContent: 'center', flexDirection: 'column' }}>
            <Box sx={{ display: 'flex', mt: 4, justifyContent: 'center' }}>
                <Typography sx={{ mr: 2, mt: 1, ml: 1 }}>
                    Gewünschte Creditpoints für das nächste Semester: <b>{props.targetCreditPoints}</b>
                </Typography>
            </Box>
            <Box sx={{ display: 'flex', mt: 4, justifyContent: 'center' }}>
                <List subheader={<ListSubheader>Wahlpflichtfächer</ListSubheader>}>
                    {props.selectedSubjects.map((s, index) => {
                        return (
                            <Box key={index}>
                                <ListItem>
                                    <ListItemText primary={s.name} secondary={s.professor} />
                                    <Typography sx={{ ml: 5 }}><b>{props.subjectPriority.get(s.id)}</b></Typography>
                                </ListItem>
                                <Divider variant="inset" component="li"></Divider>
                            </Box>
                        );
                    })}
                </List>
            </Box>
            <Box sx={{ display: 'flex', mt: 4, justifyContent: 'center' }}>
                <Typography sx={{ mr: 2, mt: 1, ml: 1 }}>
                    Überprüfen Sie die Eingaben auf ihre Richtigkeit und bestätigen Sie diese im Anschluss.
                </Typography>
            </Box>
        </Box>
    );
}