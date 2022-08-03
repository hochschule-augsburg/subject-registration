import { Box, Typography } from "@mui/material";
import { GridColDef, GridEventListener } from "@mui/x-data-grid";
import { DataGrid } from "@mui/x-data-grid/DataGrid";
import {useNavigate} from "react-router-dom";

import '../resources/css/DataGridStyles.css'

const columns: GridColDef[] = [
    { field: 'name', headerName: 'Wahlpflichtfach', width: 400 },
    { field: 'professor', headerName: 'Dozent', width: 200 },
    { field: 'registrations', headerName: 'Anmeldungen', width: 200 },
    { field: 'maxRegistrations', headerName: 'Max. Teilnehmerzahl', width: 200 },
];

const rows = [
    {id: 0, name: "Maschinelles Lernen und Musterkerennung", professor: "Prof. Dr. Max Mustermann", registrations: 5, maxRegistrations: 70},
    {id: 1, name: "Agile Softwareentwicklung (Scrum)", professor: "Prof. Dr. Max Mustermann", registrations: 75, maxRegistrations: 60},
    {id: 2, name: "Neural Networks and Deep Learning", professor: "Prof. Dr. Max Mustermann", registrations: 45, maxRegistrations: 45},
];

export function DeanPage() {
    const navigate = useNavigate();

    const onRowClick: GridEventListener<'rowClick'> = (
       params,
       event,
       details 
    ) => {
        navigate("/deanview/" + params.row.id);
    };

    return (
        <Box>
            <Box>
                <Typography variant="h4">
                    Meine Veranstaltungen
                </Typography>
                <br />
                <p>
                    Hier können Sie all Ihre Veranstaltungen mit deren Teilnehmeranmeldungen, sowie der maximalen Teilnehmerzahl einsehen.
                </p>
            </Box>
            <Box>
                <Typography variant="h6">
                    <b>Informationen zur Anmeldung</b>
                </Typography>
                <p>
                    Semester: SoSe 22
                    <br />
                    Anmeldeende: 30.05.2022
                </p>
            </Box>
            <DataGrid
                rows={rows}
                columns={columns}
                pageSize={5}
                onRowClick={onRowClick}
                rowsPerPageOptions={[5]}
                sx={{minHeight: '20rem'}}
            />
        </Box>
    );
}