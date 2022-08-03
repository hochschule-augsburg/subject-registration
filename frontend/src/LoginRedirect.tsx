import { useEffect, useState } from "react";
import Grid from "@mui/material/Grid";
import Keycloak from "keycloak-js";

interface LoginRedirectProps {
    keycloak: Keycloak.KeycloakInstance;
}

export function LoginRedirect(props: LoginRedirectProps) {
    const [delay, setDelay] = useState(0);

    useEffect(() => {
        const interval = setInterval(() => {
            if (delay === 0) {
                clearInterval(interval);
                props.keycloak.login();
            } else {
                setDelay(previousDelay => previousDelay - 1);
            }
        }, 1000);

        return () => clearInterval(interval);
    });

    return (
        <Grid
            container
            spacing={0}
            direction="column"
            alignItems="center"
            justifyContent="center"
            style={{ minHeight: "100vh" }}
        >
            <Grid item xs={3}>
                <p>Um die Wahlpflichfach-Anmeldeseite nutzen zu können müssen sie sich zuvor anmelden.</p>
                <p>Sie werden in automatisch weitergeleitet.</p>
            </Grid>
        </Grid>
    )
}