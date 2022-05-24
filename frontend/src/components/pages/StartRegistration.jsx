import Navbar from "../layout/Navbar";
import BurgerMenu from "../layout/BurgerMenu";
import { useContext, useEffect, useState } from "react";
import { URLS } from "../../App";
import userContext from "../../context/userContext";
import { RegistrationWindowControllerApi } from "@/api";

/**
 * Represents the start registration page where authorized docents can initiate the subject registration process.
 * @return {JSX.Element}
 * @constructor
 */
function StartRegistration() {
  const registrationWindowApi = new RegistrationWindowControllerApi();
  const { user } = useContext(userContext);
  const [userInfo, setUserInfo] = useState(null);

  useEffect(() => {
    const loadUser = async () => {
      const userInfo = await user.loadUserInfo();
      setUserInfo(userInfo);
    };
    if (user && !userInfo) {
      loadUser().catch(console.error);
    }
  }, [userInfo, setUserInfo]);

  /**
   * Creates a new registration process.
   * @param {MouseEvent} e
   * @return {Promise<*>}
   */
  const startRegistration = (e) => {
    const input = document.getElementsByTagName("input");

    return registrationWindowApi
      .createNewRegistrationWindow({
        semester: input[0].value,
        startDate: input[1].value,
        endDate: input[2].value,
      })
      .then((registration) => {
        console.log(registration);
        console.log("successfully added a new registration process!");
      })
      .catch((err) => {
        console.log(
          `Could not start a new registration process due to ${err}!`
        );
      });
  };

  return (
    <>
      <div className="container main">
        <div className="row">
          <h2 style={{ marginBottom: "0.75em" }}>Anmeldeprozess starten</h2>
          <p>
            Geben Sie zunächst eine Bezeichnung für den Anmeldeprozess sowie den
            Start- und Enddatum.
            <br />
            Anschließend klicken Sie auf den unten stehenden Button, um den
            Anmeldeprozess zu starten.
          </p>

          <form>
            <div className="row">
              <div className="col-6">
                <label htmlFor="formRegName">Prozessname</label>
                <input
                  type="text"
                  className="form-control"
                  id="formRegName"
                  placeholder="Prozessname eingeben"
                />
              </div>
            </div>
            <div className="row mt-2">
              <div className="col-3">
                <label htmlFor="dateStart">Startdatum</label>
                <input
                  type="datetime-local"
                  className="form-control"
                  id="dateStart"
                  defaultValue={new Date().toISOString().substr(0, 16)}
                  name="dateStart"
                />
              </div>
              <div className="col-3">
                <label htmlFor="dateStart">Enddatum</label>
                <input
                  type="datetime-local"
                  className="form-control"
                  id="dateEnd"
                  defaultValue={new Date().toISOString().substr(0, 16)}
                  name="dateEnd"
                />
              </div>
            </div>
          </form>

          <div className="row mt-4">
            <div className="col">
              <button
                className="btn btn-md btn-primary btn-block"
                style={{ textAlign: "center", width: "20%", height: "3em" }}
                type="button"
                onClick={(e) => startRegistration(e)}
              >
                Anmeldeprozess starten
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default StartRegistration;
