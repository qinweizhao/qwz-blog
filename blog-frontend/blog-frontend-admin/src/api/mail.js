import service from '@/utils/service'

const baseUrl = '/api/admin/mail'

const mailApi = {}

mailApi.testMail = mailData => {
  return service({
    url: `${baseUrl}/test`,
    method: 'post',
    data: mailData
  })
}

export default mailApi
