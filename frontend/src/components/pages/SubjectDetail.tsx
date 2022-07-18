import { useParams, Link } from "react-router-dom";
import { useGetAllSubjects } from "@/api/orval/subject-registration";
interface SubjectDetailProps {

}

/**
 * Displays all details of a subject. See RegistrationTableItem.js for a list of all props.
 * @param props
 * @return {JSX.Element}
 * @constructor
 */
function SubjectDetail(props: SubjectDetailProps) {
    const { name } = useParams();
    const subjectName = name?.replace("_", " ");

    const { isLoading, data: subjects } = useGetAllSubjects();

    let subject = undefined;

    if (subjects) {
        subject = subjects.find((s) => s.id === subjectName);
    }

    if (isLoading || !subject) {
        return <p>Fächer werden geladen...</p>
    }

    return (
        <>
            <div className="container main">
                <div className="row" style={{ marginBottom: "0.75em" }}>
                    <p style={{ color: "#F00045" }}>
                        <Link to="/subjects">
                            Übersicht
                        </Link>
                        <span> / </span>
                        <Link to=".">
                            {subject.name}
                        </Link>
                    </p>
                    <h2 style={{ marginBottom: "0.5em" }}>{subject.name}</h2>
                    {/* todo correct link */}
                    <p>
                        Detailliertere Informationen finden Sie im{" "}
                        <a href="/">Modulhandbuch.</a>
                    </p>
                </div>
                <div className="row">
                    <table className="table">
                        <tbody className="subject-detail">
                            <tr>
                                <th scope="row">Dozent</th>
                                <td>{subject ? subject.professor : ""}</td>
                            </tr>
                            <tr>
                                <th scope="row">Credit Points</th>
                                <td>{subject ? subject.creditPoints : ""}</td>
                            </tr>
                            <tr>
                                <th scope="row">Beschreibung</th>
                                <td>{subject ? subject.description : ""}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </>
    );
}

export default SubjectDetail;
