import request from '@/utils/request'

export const getAllStudents = () => 
    request.get(`/api/students`)

export const deleteStudentById = (id) => 
    request.delete(`/api/students/${id}`)