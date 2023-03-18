package com.bbs.service;

import com.bbs.domain.File;
import com.bbs.mapper.FileMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 관련 서비스
 */
@Service
@RequiredArgsConstructor
public class FileService {

	/**
	 * 파일 저장 경로
	 */
	@Value("${file.development.save-directory}")
	private String fileSaveDirectory;

	@Value("${file.development.path-slash}")
	private String pathSlash;

	/**
	 * 파일 매퍼
	 */
	private final FileMapper fileMapper;

	/**
	 * 게시글 번호로 파일 정보 조회
	 *
	 * @param articleId 게시글 번호
	 * @return File
	 */
	public File getFileByArticleId(Long articleId) {
		return fileMapper.getFileByArticleId(articleId);
	}

	/**
	 * 저장된 파일 (이미지) 를 base64 인코딩하여 반환
	 *
	 * @param file 파일 정보 객체
	 * @return
	 * @throws IOException
	 */
	public String getEncodedImageFromFile(File file) throws IOException {
		String filePath = file.getDirectory() + file.getSaveName();
		byte[] fileContent = FileUtils.readFileToByteArray(new java.io.File(filePath));
		String encodedString = Base64.getEncoder().encodeToString(fileContent);
		return encodedString;
	}

	/**
	 * temp 폴더에서 게시글 폴더로 파일 옮긴 후, DB에 파일 정보 저장
	 *
	 * @param file 파일 정보 객체
	 * @return 수행 결과
	 */
	public int inputFile(MultipartFile file, Long articleId) throws IOException {
		File fileVO = createFileVO(file, articleId);
		saveFile(file, fileVO);
		return fileMapper.inputFile(fileVO);
	}

	/**
	 * 서버 파일 및 DB 파일 정보 삭제
	 *
	 * @param previousFile 게시물 파일 정보 객체
	 * @return
	 * @throws IOException
	 */
	public int deleteFile(File previousFile) throws IOException {
		Files.delete(Paths.get(previousFile.getDirectory() + previousFile.getSaveName()));
		return fileMapper.deleteFile(previousFile);
	}

	/**
	 * 있는 경우 서버 파일, 디렉토리 및 DB 파일 정보 삭제
	 *
	 * @param previousFile 게시물 파일 정보 객체
	 * @return
	 * @throws IOException
	 */
	public int deleteFolder(File previousFile) throws IOException {
		Files.deleteIfExists(Paths.get(previousFile.getDirectory()));
		return fileMapper.deleteFile(previousFile);
	}

	/**
	 * 게시글 번호 기반 파일 정보 객체 생성
	 *
	 * @param file 파일 정보
	 * @param articleId 게시글 번호
	 * @return File
	 */
	public File createFileVO(MultipartFile file, Long articleId) {

		String saveDirectory = fileSaveDirectory + articleId + pathSlash;
		String fileName = Normalizer.normalize(file.getOriginalFilename(), Form.NFC);
		String extension = fileName.split("\\.")[1];
		String saveName = UUID.randomUUID().toString().replaceAll("-", "") + "." + extension;

		return File.builder()
			.articleId(articleId)
			.originalName(fileName)
			.saveName(saveName)
			.directory(saveDirectory)
			.extension(extension)
			.build();
	}

	/**
	 * 파일 첨부 여부 확인
	 *
	 * @param articleId 대상 게시글 번호
	 * @return
	 */
	public Boolean isFileAttached(Long articleId) {
			File file = fileMapper.getFileByArticleId(articleId);
			if (Objects.equals(null, file)) {
				return false;
			}
			return true;
		}


	/**
	 * 서버에 물리 파일 저장
	 *
	 * @param file 파일
	 * @param fileVO 파일 정보가 담긴 VO 객체
	 * @throws IOException
	 */
	private void saveFile(MultipartFile file, File fileVO) throws IOException  {
		createDirectoryFolder(fileVO.getDirectory());
		String filePath = fileVO.getDirectory() + fileVO.getSaveName();
		file.transferTo(new java.io.File(filePath));
	}

	/**
	 * 첨부 파일 저장시 게시글 번호 폴더에 저장, 폴더 존재 확인 및 생성
	 *
	 * @param directory 파일 저장 경로
	 * @throws IOException
	 */
	private void createDirectoryFolder(String directory) throws IOException {
		Path folder = Paths.get(directory);
		if (!Files.isDirectory(folder)) {
			Files.createDirectory(folder);
		}
	}
}
