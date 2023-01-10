import { defineConfig } from "orval";

export default defineConfig({
    "subject-registration": {
        output: {
            mode: "split",
            target: "src/api/orval/subject-registration.ts",
            client: "react-query",
            override: {
                mutator: {
                    path: "src/api/orval/custom-client.ts",
                    name: "customInstance"
                }
            }
        },
        input: {
            target: "http://localhost:9090/v3/api-docs",
        }
    }
});