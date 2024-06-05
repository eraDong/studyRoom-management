/*
 * @Author: eraDong 115410761+eraDong@users.noreply.github.com
 * @Date: 2024-05-31 08:41:00
 * @LastEditors: eraDong 115410761+eraDong@users.noreply.github.com
 * @LastEditTime: 2024-06-05 08:18:15
 * @FilePath: \studyRoom\src\api\student.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
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

export const existsByUsername = (username) =>
    request.get(`/api/students/existsByUsername`, {
    params: { username }
});
    