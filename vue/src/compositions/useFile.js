import {ref} from "vue";
import {useValidation} from "/@/compositions/useValidation.js";

/**
 * 파일 관련 composable
 */
export function useFile() {

  /**
   * 유효성 검증 로직 및 메세지
   */
  const {validateFile, fileValidationErrorMessage} = useValidation();

  /**
   * 이미지 데이터
   */
  const image = ref('');

  /**
   * 이미지 미리보기
   */
  const imagePreview = ref(null);


  /**
   * 이미지 미리보기 생성
   * @param file 파일
   */
  const setImagePreview = (file) => {
    const reader = new FileReader()
    reader.onload = () => {
      imagePreview.value = reader.result
    }
    reader.readAsDataURL(file)
  }

  /**
   * 이미지 업로드
   *
   * @param event 파일 업로드 이벤트
   */
  const addImage = (event) => {
    const file = event.target.files[0]

    //파일을 업로드하지 않은 채로 업로드 창을 닫은 경우 데이터 삭제
    if (file === undefined) {
      image.value = null;
      imagePreview.value = null;
      return;
    }

    validateFile(file);
    if (fileValidationErrorMessage.value.file !== '') {
      event.target.value = null;
      return;
    }

    image.value = file;

    setImagePreview(file);
  }

  return {
    image,
    imagePreview,
    fileValidationErrorMessage,
    setImagePreview,
    addImage,
  }
}