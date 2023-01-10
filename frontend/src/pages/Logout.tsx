import { useEffect } from "react";

interface LogoutProps {
    keycloak: Keycloak.KeycloakInstance;
}

export function Logout(props: LogoutProps) {
    useEffect(() => {
        props.keycloak.logout();
    }, []);

    return (<></>);
}