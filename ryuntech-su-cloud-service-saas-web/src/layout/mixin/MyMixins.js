import Pagination from '@/components/Pagination'

const MyMixins = {
  components: { Pagination },
  methods: {
    _notify(message, type = 's') {
      if (type === 's') {
        type = 'success'
      } else if (type === 'e') {
        type = 'error'
      }
      this.$message({
        message: message,
        type: type
      })
    }
  }
}
export default MyMixins
