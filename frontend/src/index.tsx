import React from "react";
import { createRoot } from "react-dom/client";
import "./resources/css/index.css";
import "bootstrap/dist/css/bootstrap.min.css";
import "./resources/css/CustomBootstrap.scss";
// uncomment when bootstrap JS is needed
//import 'bootstrap/dist/js/bootstrap.bundle';
import { App } from "./App";
import reportWebVitals from "./reportWebVitals";
import { AuthProvider } from "./context/AuthContext";
import { QueryClient, QueryClientProvider } from 'react-query';

const queryClient = new QueryClient();

const container = document.getElementById("root");
const root = createRoot(container!);

root.render(
    <React.StrictMode>
        <QueryClientProvider client={queryClient}>
            <AuthProvider>
                <App />
            </AuthProvider>
        </QueryClientProvider>
    </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
