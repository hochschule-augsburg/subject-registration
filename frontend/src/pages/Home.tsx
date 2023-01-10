import { Link, Typography } from "@mui/material";
import Box from "@mui/material/Box";

export function Home() {
    return (
        <Box>
            <Box>
                <Typography variant="h4">
                    Startseite
                </Typography>
                <br />
                <p>
                    Herzlich Willkommen bei der WPF-Anmeldeseite!
                    <br />
                    Auf der linken Seite finden Sie ale nötigen Informationen zur
                    Anmeldung.
                </p>
            </Box>
            <Box>
                <Typography variant="h6">
                    Anmeldefrist
                </Typography>
                <p>
                    Die Anmeldefrist für die Wahlpflichtfächer beginnt am 29.4.2021 und
                    endet am 30.05.2021.
                </p>
            </Box>
            <Box>
                <Typography variant="h6">
                    Kontakt
                </Typography>
                <p>
                Bei Fragen/Problemen wenden Sie sich bitte an Frau Bäurle.
                </p>
            </Box>
            <Box>
                <Typography variant="h6">
                    Impressum
                </Typography>
                <p>
                    Das Impressum finden Sie unter folgender <Link href="https://www.hs-augsburg.de/Service/Impressum.html">Adresse</Link>.
                </p>
            </Box>
        </Box>
    );
}