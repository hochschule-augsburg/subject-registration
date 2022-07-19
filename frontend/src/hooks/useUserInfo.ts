import { useContext } from "react";
import { useQuery } from "react-query";
import userContext from "@/context/userContext";

function useUserInfo() {
    const { user, setUser } = useContext(userContext);

    return useQuery("userInfo", async () => {
        const userInfo = await user.loadUserInfo();
        return userInfo;
    });
}

export { useUserInfo };