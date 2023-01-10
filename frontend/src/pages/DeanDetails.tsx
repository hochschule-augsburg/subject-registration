import { Box, Typography } from "@mui/material";
import { DataGrid, GridColDef } from "@mui/x-data-grid";
import { useParams } from "react-router-dom";

const columns: GridColDef[] = [
    { field: 'name', headerName: 'Vorname', width: 200 },
    { field: 'surname', headerName: 'Nachname', width: 200 },
    { field: 'number', headerName: 'Matrikelnummer', width: 200 },
    { field: 'semester', headerName: 'Studiengang/Semester', width: 200 },
    { field: 'priority', headerName: 'Prioritätenpunkte', width: 200 },
];

const rows = [
    {id: 0, name: "Franz", surname: "Wilhelm", number: "205846", semester: "WI 2", priority: "80"},
    {id: 1, name: "Josef", surname: "Walter", number: "252846", semester: "IN 3", priority: "75"},
    {id: 2, name: "Willy", surname: "Hans", number: "435846", semester: "IN 6", priority: "72"},
    {id: 3, name: "Dominik", surname: "Mayer", number: "632578", semester: "WI 8", priority: "60"},
    {id: 4, name: "Friedrich", surname: "Walter", number: "555846", semester: "WI 2", priority: "10"},
];

export function DeanDetails() {
    let { id } = useParams();

    let title = "";
    switch (id) {
        case "0": title = "Maschinelles Lernen und Musterkerennung"; break;
        case "1": title = "Agile Softwareentwicklung (Scrum)"; break;
        case "2": title = "Neural Networks and Deep Learning"; break;
    }

    let desc = "";
    switch (id) {
        case "0": desc = "Grundlagen der Mustererkennung: Vorverarbeitung und Merkmalsextraktion, Performanzmaße, Einfache Klassifikatoren (z.B Minimum-Distanz Klassifikatoren), Probalistische Klassifikatoren, Unüberwachtes Lernen / Clustering, Neuronale Netze, Deep Learning Ansätze."; break;
        case "1": desc = "Grundlagen: Klassische und agile Entwicklungsmethoden, Agiles Manifest, Iteratives Vorgehen. Scrum: Grundlagen und Motivation, Anforderungsmanagement, Rollen und Meetings, Sprints und Vorgehen, Releaseplanung. Das Team: Phasen der Teamentwicklung, Persönlichkeitsprofile"; break;
        case "2": desc = "Grundlagen der Mustererkennung: Vorverarbeitung und Merkmalsextraktion, Performanzmaße, Einfache Klassifikatoren (z.B Minimum-Distanz Klassifikatoren), Probalistische Klassifikatoren, Unüberwachtes Lernen / Clustering, Neuronale Netze, Deep Learning Ansätze."; break;
    }

    return (
        <Box>
            <Box>
                <Typography variant="h4">
                    {title}
                </Typography>
                <br/>
                <Box>
                    <Typography variant="h6">
                        <b>Informationen zur Veranstaltung</b>
                    </Typography>
                    <p>
                        Credit Points: <b>5</b>
                        <p/>
                        Beschreibung: {desc}
                    </p>
                </Box>
            </Box>
            <DataGrid
                rows={rows}
                columns={columns}
                pageSize={10}
                sx={{minHeight: '30rem'}}
            />
        </Box>
    );
}