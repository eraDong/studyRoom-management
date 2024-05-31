import request from '@/utils/request'

export const getAllStudents = () => 
    request.get(`/api/students`)

export const deleteStudentById = (id) => 
    request.delete(`/api/students/${id}`)

export const updateStudents = (id, data) =>
    request.put(`/api/students/${id}`, data)

export const addStudent = (data) =>
    request.post(`/api/students/create`, data)

export const getStudentById = (id) =>
    request.get(`/api/students/${id}`)