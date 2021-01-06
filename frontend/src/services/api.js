import axios from 'axios';
import { properties } from '@/config/services-config'

const api = axios.create({
    baseURL: properties.apiURL,
});

export default api;