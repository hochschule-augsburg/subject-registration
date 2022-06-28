import Pagination from "../layout/Pagination";
import { COURSE_CATALOGUE } from "@/server_constants";
import { useGetAllSubjects } from "@/api/orval/subject-registration";

// defines the max. number of subjects that should be shown on one page.
const SUBJECT_LIMIT = 16;
// defines the max. number of page numbers that should be shown on the pagination component.
const PAGE_LIMIT = 4;
const NO_SUBJECTS = "Momentan sind keine Wahlpflichtfächer vorhanden.";

/**
 * Provides an overview of all available subjects.
 */
function SubjectOverview() {
    return (
        <>
            <div className="container main">
                <div className="row">
                    <h2 style={{ marginBottom: "0.75em" }}>
                        Übersicht Wahlpflichtfächer
                    </h2>
                    <p>
                        Hier finden Sie alle Wahlpflichtfächer, die in diesem Semester
                        angeboten werden.
                        <br />
                        Wenn Sie sich für einen bestimmten Wahlpflichtfach anmelden möchten,
                        klicken Sie auf 'Anmelden' in der Aktionsleiste.
                        <br />
                        Detaillierte Informationen zu den Wahlpflichtfächern finden Sie im{" "}
                        <a
                            href={
                                // userInfo.degreeCourse is normally read, but doesn't exist
                                COURSE_CATALOGUE.BACHELOR
                            }
                        >
                            Modulhandbuch
                        </a>
                        .
                    </p>
                    <Subjects />
                </div>
            </div>
            <br />
        </>
    );
}

function Subjects() {
    const { isLoading, data: subjects } = useGetAllSubjects();

    if (isLoading) {
        return <p>Fächer werden geladen...</p>
    }

    if (!subjects|| subjects.length <= 0) {
        return (<p>{NO_SUBJECTS}</p>);
    }
    return (<Pagination
        // @ts-ignore
        data={subjects}
        dataLimit={SUBJECT_LIMIT}
        pageLimit={PAGE_LIMIT}
    />);
}

export default SubjectOverview;
