import { useEffect, useState } from "react";
import Keycloak from "keycloak-js";
import { LoginRedirect } from "./LoginRedirect";
import { AppBar, Box, CssBaseline, Tab, Tabs, Toolbar, Typography } from "@mui/material";
import { Link, Route, BrowserRouter as Router, Routes, useLocation, matchPath } from "react-router-dom";
import { Subjects } from "./pages/Subjects";
import { useAuthDispatch } from "./context/AuthContext";
import { StepperPage } from "./pages/StepperPage";
import { DeanPage } from "./pages/DeanPage";
import { DeanDetails } from "./pages/DeanDetails";

import "./resources/css/AppTabsStyles.css";
import { WindowPage } from "./pages/WindowPage";

type AuthStatus = "pending" | "authenticated" | "unauthenticated";

function App() {
    const [authStatus, setAuthStatus] = useState<AuthStatus>("pending");
    const [keycloak, setKeycloak] = useState<Keycloak.KeycloakInstance>();
    const [userId, setUserId] = useState<string | undefined>("");
    const dispatchToken = useAuthDispatch();

    useEffect(() => {
        const keycloak = Keycloak("/keycloak.json");
        keycloak.onTokenExpired = () => {
            keycloak.updateToken(30).then((refreshed) => {
                if (refreshed) {
                    console.log("Successfully refreshed token");
                }
                if (keycloak.token) {
                    dispatchToken(keycloak.token);
                }
            }).catch(() => {
                console.log("Unable to refresh token, logging out...");
                keycloak.logout();
            });
        };
        keycloak.init({}).then(authenticated => {
            console.log(authenticated ? "authenticated" : "not authenticated");
            if (authenticated) {
                setAuthStatus("authenticated");
                if (keycloak.token) {
                    dispatchToken(keycloak.token);
                    setUserId(keycloak.idTokenParsed?.sub);
                }
            } else {
                setKeycloak(keycloak);
                setAuthStatus("unauthenticated");
            }
        }).catch(() => {
            console.log("failed to initialize");
        });
    }, []);

    if (authStatus === "authenticated" && keycloak) {
        return (<MainPage keycloak={keycloak} userId={userId} />);
    }
    if (authStatus === "unauthenticated" && keycloak) {
        return (<LoginRedirect keycloak={keycloak} />)
    }
    return (<p>Pending...</p>)
}

interface MainPageProps {
    keycloak: Keycloak.KeycloakInstance;
    userId: string | undefined;
}

function useRouteMatch(patterns: readonly string[]) {
    const { pathname } = useLocation();

    for (let i = 0; i < patterns.length; i += 1) {
        const pattern = patterns[0];
        const possibleMatch = matchPath(pattern, pathname);
        if (possibleMatch !== null) {
            return possibleMatch;
        }
    }

    return null;
}

interface CustomTabsProps {
    clickHandler: () => void;
}

function CustomTabs(props: CustomTabsProps) {
    const { pathname } = useLocation();

    const routeMatch = useRouteMatch(['/registrations', '/deanview', '/window']);
    const currentTab = routeMatch?.pattern?.path;

    return (
        <Tabs value={pathname} sx={{ ml: '10rem' }} >
            <Tab label="Meine Anmeldungen" value="/registrations" to="/registrations" component={Link} />
            <Tab label="Meine Veranstaltungen" value="/deanview" to="/deanview" component={Link} />
            <Tab label="Anmeldungsfenster" value="/window" to="/window" component={Link} />
            <Tab label="Abmelden" onClick={props.clickHandler} />
        </Tabs>
    );
}

function MainPage(props: MainPageProps) {
    const logout = () => {
        props.keycloak.logout();
    };

    return (
        <Router>
            <Box sx={{ display: 'flex', width: '100%', alignItems: 'center', justifyContent: 'center' }}>
                <CssBaseline />
                <AppBar
                    position="fixed"
                    sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}
                    style={{ background: '#F00045' }}
                >
                    <Toolbar >
                        <Typography variant="h6" noWrap component="div">
                            WPF Anmeldung
                        </Typography>
                        <CustomTabs clickHandler={logout} />
                    </Toolbar>
                </AppBar>
                <Box
                    component="main"
                    sx={{ flexGrow: 1, bgcolor: 'background.default', p: 3, maxWidth: 1200, ml: '10px' }}
                >
                    <Toolbar />

                    <Routes>
                        <Route
                            path="/registrations"
                            element={<StepperPage userId={props.userId} />}
                        />
                        <Route
                            path="/deanview"
                            element={<DeanPage />}
                        />
                        <Route
                            path="/deanview/:id"
                            element={<DeanDetails />}
                        />
                        <Route
                            path="/window"
                            element={<WindowPage />}
                        />
                        <Route
                            path="/subjects"
                            element={<Subjects />}
                        />
                    </Routes>
                </Box>
            </Box>
        </Router>
    );
}

export { App };