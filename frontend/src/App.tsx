import "./resources/css/App.css";
import "./resources/css/BurgerMenu.css";
import Home from "./components/pages/Home";
import Navbar from "./components/layout/Navbar";
import BurgerMenu from "./components/layout/BurgerMenu";
import MyRegistrations from "./components/pages/MyRegistrations";
import StartRegistration from "./components/pages/StartRegistration";
import SubjectOverview from "./components/pages/SubjectOverview";
import SubjectDetail from "./components/pages/SubjectDetail";
import Info from "./components/pages/Info";
import {
    BrowserRouter as Router,
    Route,
    Routes,
    Outlet,
} from "react-router-dom";
import { useEffect, useState } from "react";
import userContext from "./context/userContext";
import SubjectSelectionContext from "./context/subjectSelectionContext";
import Keycloak from "keycloak-js";
import { URLS } from "./server_constants";
import { useAuthDispatch } from "./context/AuthContext";

function App() {
    const [user, setUser] = useState<Keycloak.KeycloakInstance | null>(null);
    const [userInfo, setUserInfo] = useState<any>(null);
    const [subjectSelection, setSubjectSelection] = useState(null);
    const [keycloak, setKeycloak] = useState<Keycloak.KeycloakInstance | null>(null);
    const [authenticated, setAuthenticated] = useState<boolean | undefined>(false);
    const dispatchToken = useAuthDispatch();

    useEffect(() => {
        const createKeycloak = async () => {
            const keycloak = Keycloak("/keycloak.json");

            await keycloak.init({
                onLoad: "login-required",
            });
            setKeycloak(keycloak);
            if (keycloak.token) {
                dispatchToken(keycloak.token);
            }
            setAuthenticated(keycloak.authenticated);
            setUser(keycloak);
            const userInfo = await keycloak.loadUserInfo();
            setUserInfo(userInfo);
        };

        if (!keycloak) {
            // The timeout is needed to prevent an infinite redirect bug when
            // using <StrictMode>, see: https://github.com/react-keycloak/react-keycloak/issues/182
            // In pruduction this should have no noticeable difference, since
            // a timeout with 0 is executed on the next event cycle
            const timer = setTimeout(() => {
                createKeycloak().catch(console.error);
            }, 0);

            return () => {
                clearTimeout(timer);
            };
        }

        return () => { };
    }, []);

    return (
        <>
            {keycloak ? (
                authenticated ? (
                    <div>
                        <userContext.Provider value={{
                            // @ts-ignore
                            user,
                            setUser
                        }}>
                            <SubjectSelectionContext.Provider
                                value={{
                                    // @ts-ignore
                                    subjectSelection, setSubjectSelection
                                }}
                            >
                                    <Router>
                                        <Routes>
                                            <Route
                                                path="/"
                                                element={
                                                    <>
                                                        <Navbar />
                                                        <BurgerMenu
                                                            name={URLS.HOME}
                                                            username={
                                                                userInfo
                                                                    ? `${userInfo.given_name} ${userInfo.family_name}`
                                                                    : ""
                                                            }
                                                            major={userInfo ? userInfo.degreeCourse : ""}
                                                            preferred_username={
                                                                userInfo ? userInfo.preferred_username : ""
                                                            }
                                                            // @ts-ignore
                                                            logout={user ? user.logout : null}
                                                            timestamp={
                                                                userInfo ? userInfo.createTimestamp : "20210911"
                                                            }
                                                        />
                                                        <Outlet />
                                                    </>
                                                }
                                            >
                                                <Route index element={<Home />} />
                                                <Route
                                                    path={`/${URLS.REGISTRATIONS}`}
                                                    element={<MyRegistrations />}
                                                />
                                                <Route
                                                    path={`/${URLS.START_REGISTRATION}`}
                                                    element={<StartRegistration />}
                                                />
                                                <Route
                                                    path={`/${URLS.SUBJECTS}`}
                                                    element={<SubjectOverview />}
                                                />
                                                <Route
                                                    path={`/${URLS.SUBJECTS}/:name`}
                                                    element={<SubjectDetail />}
                                                />
                                                <Route path={`/${URLS.INFO}`} element={<Info />} />
                                            </Route>
                                        </Routes>
                                    </Router>
                            </SubjectSelectionContext.Provider>
                        </userContext.Provider>
                    </div>
                ) : (
                    <div>
                        <p>Error: Authentication failed!</p>
                    </div>
                )
            ) : (
                <div>
                    <p>Starting keycloak service...</p>
                </div>
            )}
        </>
    );
}

export default App;
