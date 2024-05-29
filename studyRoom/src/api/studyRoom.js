import request from '@/utils/request'

export const getAllStudyRooms = () => 
    request.get(`/api/studyrooms`)

export const getStudyRoomById = (id) => 
    request.get(`/api/studyrooms/${id}`)

export const deleteStudyRoomById = (id) => 
    request.delete(`/api/studyrooms/${id}`)

export const updateStudyRoom = (id, data) =>
    request.put(`/api/studyrooms/${id}`, data)

export const addStudyRoom = (data) =>
    request.post(`/api/studyrooms/create`, data)


