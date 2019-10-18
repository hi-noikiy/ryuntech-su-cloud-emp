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
    },
    gotoPage(path, query = {}) {
      this.$router.push({ path: path, query: query })
    }
  }
}
export default MyMixins
