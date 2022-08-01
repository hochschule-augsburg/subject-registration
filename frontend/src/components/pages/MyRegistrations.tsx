import { URLS } from "@/server_constants";
import React, { useContext, useEffect, useState } from "react";
import { Link } from "react-router-dom";
import RegistrationTableItem from "../RegistrationTableItem";
import SubjectSelectionContext from "../../context/subjectSelectionContext";
import userContext from "../../context/userContext";
import { RegistrationControllerApi, RegistrationTO } from "@/api";
import { getRequestHeaders } from "../../util/util";
import Keycloak from "keycloak-js";
import { useCreateNewRegistration, useGetAllRegistrations } from "@/api/orval/subject-registration";
import { useUserInfo } from "@/hooks/useUserInfo";

const REG_STATUS = {
    RECEIVED: "Antrag eingegangen",
    REJECTED: "Antrag abgelehnt",
};

const REG_BTN_MAP = {
    CREATE: "Anmeldung abschließen",
    EDIT: "Anmeldung überarbeiten",
};

/**
 * Displays the current registration of the user and all subjects associated with it.
 * @return {JSX.Element}
 * @constructor
 */
function MyRegistrations() {
    const registrationApi = new RegistrationControllerApi();
    const { user, setUser } = useContext(userContext);
    // const [userInfo, setUserInfo] = useState<any>(null);
    // const [registration, setRegistration] = useState<RegistrationTO | null>(null);
    const { subjectSelection } = useContext(SubjectSelectionContext) || {};

    const { isLoading, data: registrations } = useGetAllRegistrations();
    const subjectMutation = useCreateNewRegistration();
    const { data: userInfo } = useUserInfo();

    /**
     * Check if all input from the user is valid (unsigned numbers only).
     * @param {HTMLCollection} input All user input values.
     * @return {boolean} Returns true if an invalid input was found; otherwise false.
     */
    const validateInput = (input: HTMLCollection) => {
        let hasInvalidInput = false;
        for (let i = 0; i < input.length; i++) {
            // @ts-ignore
            if (input[i].value.match(/^\d+$/)) {
                input[i].classList.remove("is-invalid");
            } else {
                hasInvalidInput = true;
                input[i].classList.add("is-invalid");
            }
        }
        return hasInvalidInput;
    };

    /**
     * Submit the user's subject registration.
     * @param {MouseEvent} e Mouse event instance.
     */
    const handleRegistration = (e: React.SyntheticEvent) => {
        e.preventDefault();
        const input = document.getElementsByTagName("input");

        let validInput = validateInput(input);
        validInput = true;

        if (validInput) {
            console.log(
                `[MyRegistrations][completeRegistration] with cp ${input[0].value}!`
            );
        } else {
            console.log(
                `MyRegistrations][completeRegistration] Invalid input detected!`
            );
            return;
        }
        if (registrations && registrations.length > 0) {
            // todo api not tested yet
            // update existing registration of the user if it was already created
            return registrationApi
                .updateRegistration(
                    {
                        id: user.subject!,
                        subjectSelection: [
                            {
                                // @ts-ignore
                                registration:
                                    "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                // @ts-ignore
                                subject: "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                                points: 1,
                            },
                        ],
                    },
                    getRequestHeaders(user)
                )
                .then((response) => {
                    console.log("successful call");
                })
                .catch((err) => {
                    console.log(`error! ${err}`);
                });
        } else {
            // create a new registration for the user. only works if a registration window is active
            // todo correct subject selection
            const arr = [] as any[];
            subjectSelection!.forEach((subject) => {
                arr.push({
                    registration: {
                        id: user.subject,
                        student: user.idTokenParsed!.preferred_username,
                    },
                    subject: {
                        id: subject.id,
                        name: subject.name,
                        professor: subject.professor,
                        creditPoints: 10,
                        capacity: subject.capacity,
                        status: subject.status,
                        description: subject.description,
                        specialization: subject.specialization,
                    },
                    points: 10,
                });
            });

            console.log(`create new registration with ${JSON.stringify(arr)}`);

            registrationApi
                .createNewRegistration({
                    student: user.idTokenParsed!.preferred_username,
                    subjectSelection: arr,
                })
                .then((response) => {
                    console.log("successful call");
                })
                .catch((err) => {
                    console.log(`error! ${err}`);
                });

            subjectMutation.mutate({
                data: {
                    // @ts-ignore
                    //student: user.idTokenParsed!.preferred_username,
                    // @ts-ignore
                    student: userInfo.sub,
                    subjectSelection: [
                        {
                            subject: "00000001-0000-0000-0000-000000000000",
                            // subject: "1",
                            // subject: "00000001000000000000000000000000",
                            points: 900
                        }
                    ],
                }
            });
        }
    };

    return (
        <>
            <div className="container main">
                <div className="row">
                    <h2>Meine Anmeldungen</h2>
                    <p>
                        Hier können Sie alle Anmeldungen einsehen, die Sie bisher getätigt
                        haben.
                    </p>
                </div>
                <div className="row">
                    <h5>Informationen zum Anmeldeverfahren</h5>
                    <ul style={{ listStyleType: "none" }}>
                        <li>Semester: SoSe 21</li>
                        <li>Anmeldefrist: 30.05.2021</li>
                        <li style={{ display: "flex" }}>
                            Gewünschte Anzahl Credits (dieses Semester):
                            <form className="form-inline" noValidate={true}>
                                <input
                                    type="text"
                                    style={{ marginLeft: "0.75em", width: "5em" }}
                                    required
                                />
                                <div
                                    className="invalid-feedback"
                                    style={{ marginLeft: "0.75em" }}
                                >
                                    Eingabe ungültig
                                </div>
                            </form>
                        </li>
                    </ul>
                </div>
                <div className="row">
                    {subjectSelection && subjectSelection.length > 0 ? (
                        <>
                            <div className="row">
                                <h5>Informationen zur Anmeldung</h5>
                                <p>
                                    Weisen Sie ihren gewünschten Wahlpflichtfächern Prio-Punkte
                                    zu.
                                    <br />
                                    Je mehr Punkte, desto höher die Chance, den Platz in dem
                                    gewünschten Fach zu bekommen.
                                    <br /> Sie müssen in Summe <b>exakt</b> 1000 Punkte vergeben.
                                </p>
                                <table className="table">
                                    <thead>
                                        <tr>
                                            <th scope="col">Wahlpflichtfach</th>
                                            <th scope="col">Dozent</th>
                                            <th scope="col">CP</th>
                                            <th scope="col">Priorität</th>
                                            <th scope="col">Status</th>
                                            <th scope="col">Aktion</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {subjectSelection.map((subject) => (
                                            <RegistrationTableItem
                                                key={subject.id.toString()}
                                                id={subject.id}
                                                subject={subject.name}
                                                professor={subject.professor}
                                                creditPoints={subject.creditPoints}
                                                // priority does not exist on subjecto
                                                priority={0}
                                                status={REG_STATUS.RECEIVED}
                                                description={subject.description!}
                                                specialization={subject.specialization!}
                                            />
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                            {/* Create / edit registration button */}
                            <div className="row">
                                <button
                                    className="btn btn-md btn-primary btn-block"
                                    style={{ textAlign: "center", width: "20%", height: "3em" }}
                                    type="button"
                                    onClick={(e) => handleRegistration(e)}
                                >
                                    {(registrations && registrations.length > 0) ? REG_BTN_MAP.EDIT : REG_BTN_MAP.CREATE}
                                </button>
                            </div>
                        </>
                    ) : (
                        <div className="row">
                            <p>
                                Sie haben sich bisher noch für kein Wahlpflichtfach angemeldet.
                                <br />
                                Auf der <Link to={`/${URLS.SUBJECTS}`}>
                                    Übersichtsseite
                                </Link>{" "}
                                können Sie alle Wahlpflichtfächer, die in diesem Semester
                                angeboten werden, einsehen.
                            </p>
                        </div>
                    )}
                </div>
            </div>
        </>
    );
}

export default MyRegistrations;
