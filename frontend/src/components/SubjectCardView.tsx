import { URLS } from "@/server_constants";
import { useNavigate, useLocation } from "react-router-dom";
import React, { useContext } from "react";
import SubjectSelectionContext from "../context/subjectSelectionContext";

const ACTION = "Aktion";
const NO_DESCRIPTION = "Keine Beschreibung vorhanden.";
const ENROLL_STATUS = {
    REGISTER: "Anmelden",
    UNREGISTER: "Abmelden",
};

interface SubjectCardViewProps {
    /**
     * ID of the subject.
     */
    id: string,

    /**
     * Name of the subject.
     */
    name: string,

    /**
     * Name of the subject.
     */
    subject: string,

    /**
     * Name of the professor.
     */
    professor: string,

    /**
     * Credit points of the subject.
     */
    creditPoints: number,

    /**
     * Short description text of the subject.
     */
    description: string,

    /**
      * Specialization which is associated with this subject.
      */
    specialization: string,

    /**
     * Defines how many students can take part in this subject
     */
    capacity: number,

    /**
     * True if this subject is available; otherwise false.
     */
    status: boolean,

    /**
     * True => show user that they can register for this subject; False => show user that they
 * can unregister from this subject.
     */
    enroll: boolean
}

function SubjectCardView(props: SubjectCardViewProps) {
    let navigate = useNavigate();
    const location = useLocation();

    const { subjectSelection, setSubjectSelection } = useContext(
        SubjectSelectionContext
    ) || {};

    if (!subjectSelection || !setSubjectSelection) {
        return <></>;
    }

    /**
     * Handle clicks on a subject title.
     * @param {MouseEvent} e Instance of the mouse event.
     */
    const handleSubjectClick = (e: React.SyntheticEvent) => {
        const link = props.subject.replace(" ", "_");
        console.log(
            `[SubjectCardView][handleSubjectClick] clicked on the subject ${link}!`
        );
        navigate(`${URLS.SUBJECTS}/${link}`, {
            state: {
                subject: props,
                prevPath: location.pathname,
            },
        });
        e.preventDefault();
    };

    /**
     * Handle clicks on the register/unregister button of a subject card. Opens the subject detail page.
     * @param {MouseEvent} e Instance of the mouse event.
     */
    const handleEnrollClick = (e: React.SyntheticEvent) => {
        e.preventDefault();
        console.log(
            `[SubjectCardView][handleEnrollClick] clicked on the enroll button of the subject ${props.subject}!`
        );
        let subjects = subjectSelection ? Array.from(subjectSelection) : [];
        if (props.enroll) {
            subjects.push({
                id: props.id,
                name: props.subject,
                creditPoints: props.creditPoints,
                professor: props.professor,
                // priority: 0,
                description: props.description,
                specialization: props.specialization,
                capacity: props.capacity,
                status: props.status,
            });
        } else {
            subjects = subjects.filter((subject) => subject.id !== props.id);
        }
        console.log(`subject selection: ${JSON.stringify(subjects)}`);
        setSubjectSelection(subjects);
    };

    return (
        <div className="col">
            <div className="card">
                <div className="card-body subject-card">
                    <h5 className="card-title" onClick={(e) => handleSubjectClick(e)}>
                        {props.subject}
                    </h5>
                    <h6 className="card-subtitle">
                        {props.professor} | {props.creditPoints} CP
                    </h6>
                    <p className="card-text subject-card-description">
                        {props.description ? props.description : NO_DESCRIPTION}
                    </p>
                    <p
                        className="card-text"
                        style={{ marginBottom: "0", color: "#4D4D4D", fontWeight: 600 }}
                    >
                        {ACTION}
                    </p>
                    <a
                        href="/"
                        className="card-link"
                        onClick={(e) => handleEnrollClick(e)}
                    >
                        {props.enroll ? ENROLL_STATUS.REGISTER : ENROLL_STATUS.UNREGISTER}
                    </a>
                </div>
            </div>
        </div>
    );
}

export default SubjectCardView;
