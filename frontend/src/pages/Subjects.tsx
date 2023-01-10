import { useGetAllSubjects } from "@/api/orval/subject-registration";
import { SubjectTO } from "@/api/orval/subject-registration.schemas";
import { Button, Card, CardActions, CardContent, Dialog, Grid, styled, Typography } from "@mui/material";
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';
import Box from "@mui/material/Box";
import CircularProgress from '@mui/material/CircularProgress';
import { useState } from "react";

export function Subjects() {
    const { isLoading, data: subjects } = useGetAllSubjects();

    if (isLoading) {
        return (
            <Box sx={{ display: 'flex' }}>
                <CircularProgress />
            </Box>
        );
    }

    return (
        <Grid container spacing={2}>
            {subjects?.map((subject: SubjectTO) => (
                <SubjectCard key={subject.id} subject={subject} />
            ))}
        </Grid>
    );
}

interface SubjectCardProps {
    subject: SubjectTO;
}

function SubjectCard(props: SubjectCardProps) {
    const [open, setOpen] = useState(false);

    const showMoreDetails = () => {
        setOpen(true);
    };

    function callback() {
        setOpen(false);
    }

    return (
        <Grid item xs={3}>
            <Card variant="outlined">
                <CardContent>
                    <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
                        {"Bachelor (" + props.subject.creditPoints + " CP)"}
                    </Typography>
                    <Typography variant="h5" component="div">
                        {props.subject.name}
                    </Typography>
                    <Typography sx={{ mb: 1.5 }} color="text.secondary">
                        {props.subject.professor}
                    </Typography>
                    <Typography variant="body2" sx={{
                        display: '-webkit-box',
                        overflow: 'hidden',
                        WebkitBoxOrient: 'vertical',
                        WebkitLineClamp: 6,
                    }}>
                        {props.subject.description}
                    </Typography>
                </CardContent>
                <CardActions>
                    <Button size="small" onClick={showMoreDetails}>Mehr Details</Button>
                </CardActions>
            </Card>
            <SubjectDetailDialog subject={props.subject} open={open} closeCallback={callback} />
        </Grid>
    );
}

export const BootstrapDialog = styled(Dialog)(({ theme }) => ({
    '& .MuiDialogContent-root': {
        padding: theme.spacing(2),
    },
    '& .MuiDialogActions-root': {
        padding: theme.spacing(1),
    },
}));

export interface DialogTitleProps {
    id: string;
    children?: React.ReactNode;
    onClose: () => void;
}

export function BootstrapDialogTitle(props: DialogTitleProps) {
    const { children, onClose, ...other } = props;

    return (
        <DialogTitle sx={{ m: 0, p: 2 }} {...other}>
            {children}
            {onClose ? (
                <IconButton
                    aria-label="close"
                    onClick={onClose}
                    sx={{
                        position: 'absolute',
                        right: 8,
                        top: 8,
                        color: (theme) => theme.palette.grey[500],
                    }}
                >
                    <CloseIcon />
                </IconButton>
            ) : null}
        </DialogTitle>
    );
}

interface SubjectDetailDialogProps {
    subject: SubjectTO;
    open: boolean;
    closeCallback: () => void;
}

function SubjectDetailDialog(props: SubjectDetailDialogProps) {
    return (
        <BootstrapDialog open={props.open} scroll="paper">
            <BootstrapDialogTitle id="customized-dialog-title" onClose={props.closeCallback}>
                Modal title
            </BootstrapDialogTitle>
            <DialogContent dividers>
                <Typography gutterBottom>
                    Cras mattis consectetur purus sit amet fermentum. Cras justo odio,
                    dapibus ac facilisis in, egestas eget quam. Morbi leo risus, porta ac
                    consectetur ac, vestibulum at eros.
                </Typography>
                <Typography gutterBottom>
                    Praesent commodo cursus magna, vel scelerisque nisl consectetur et.
                    Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor auctor.
                </Typography>
                <Typography gutterBottom>
                    Aenean lacinia bibendum nulla sed consectetur. Praesent commodo cursus
                    magna, vel scelerisque nisl consectetur et. Donec sed odio dui. Donec
                    ullamcorper nulla non metus auctor fringilla.
                </Typography>
            </DialogContent>
            <DialogActions>
                <Button autoFocus onClick={props.closeCallback}>
                    Save changes
                </Button>
            </DialogActions>
        </BootstrapDialog>
    );
}