import Axios, { AxiosError, AxiosRequestConfig } from 'axios';

export const AXIOS_INSTANCE = createAxiosInstance();

export const customInstance = <T>(config: AxiosRequestConfig): Promise<T> => {
  const source = Axios.CancelToken.source();
  const promise = AXIOS_INSTANCE({ ...config, cancelToken: source.token }).then(
    ({ data }) => data,
  );

  // @ts-ignore
  promise.cancel = () => {
    source.cancel('Query was cancelled by React Query');
  };

  return promise;
};

function createAxiosInstance() {
  // if this is the case, we are probably on the deployment server
  if (window.location.hostname.includes("hs-augsburg.de")) {
    return Axios.create({ baseURL: window.location.hostname + '/api/' });
  }
  return Axios.create({ baseURL: 'http://localhost:9090/' });
}

export default customInstance;

export interface ErrorType<Error> extends AxiosError<Error> { }